package com.example.onurb.playlistrecommender;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.onurb.playlistrecommender.classes.ApiResponseImagga;
import com.example.onurb.playlistrecommender.classes.ApiResponseSpotify;
import com.example.onurb.playlistrecommender.classes.Image;
import com.example.onurb.playlistrecommender.classes.JsonPlaceHolderApi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;


import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainScreenActivity extends AppCompatActivity {

    private LinearLayout weather_layout, mood_layout, image_layout;
    public static final int GET_FROM_GALLERY = 3;
    public static final int GET_FROM_CAMERA = 5;


    final String username = "acc_9cb3ed6f96f78e2";
    final String password = "2d58ec1ac311aa0bdd68715540bf1b60";

    private String[] tags = {"Tag", "Tag", "Tag", "Tag", "Tag"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Get the linear layouts located in the screen //
        weather_layout = (LinearLayout) findViewById(R.id.ll1);
        mood_layout = (LinearLayout) findViewById(R.id.ll2);
        image_layout = (LinearLayout) findViewById(R.id.ll3);

        // Assigning onClickListeners //
        weather_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent weather_intent = new Intent(MainScreenActivity.this, MapsActivity.class);
                startActivity(weather_intent);
            }
        }); // End of onClickListener for weather //

        mood_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mood_intent = new Intent(MainScreenActivity.this, MoodSelectActivity.class);
                startActivity(mood_intent);
            }
        }); // End of onClicListener for mood //

        image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPhotoUploadDialog();
            }
        }); // End of onClicListener for image upload //

    } // End of onCreate method //


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If we succesfully selected image from the gallery //
        if(requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK && data != null)
        {
            // Getting the data of photo //
            Uri selected_image_uri = data.getData();
            // Create the dialog to show the photo //
            showUploadedPhotoDialog(selected_image_uri);
        }
        else if(requestCode == GET_FROM_CAMERA && resultCode == RESULT_OK && data != null)
        {
            // Getting the data of photo //
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            // Create the dialog to show the photo //
            showCapturedPhoto(bitmap);
        }
    }

    // Photo Upload Dialog Creation Function //
    public void showPhotoUploadDialog()
    {
        // Creating dialog object //
        final Dialog photo_dialog = new Dialog(this);
        photo_dialog.setContentView(R.layout.photo_upload_layout);

        // Getting dialog elements //
        ImageView gallery = (ImageView) photo_dialog.findViewById(R.id.upload_imageView1);
        ImageView camera = (ImageView) photo_dialog.findViewById(R.id.upload_imageView2);
        ImageView from_url = (ImageView) photo_dialog.findViewById(R.id.upload_imageView3);

        // OnClick Functions of items //
        // onClick funcion of gallery icon //
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Open gallery //
                Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery_intent, GET_FROM_GALLERY);
            }
        }); // End of onClick function of gallery icon //

        // onClick function of camera icon //
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Open camera //
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent,GET_FROM_CAMERA);
            }
        }); // End of onClick function of camera icon //


        from_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog photo_upload_dialog = new Dialog(MainScreenActivity.this);
                photo_upload_dialog.setContentView(R.layout.enter_photo_url_layout);

                // Get the edittext and button on the dialog //
                final EditText url_edittext = (EditText) photo_upload_dialog.findViewById(R.id.photo_url);
                Button ok_button_on_url = (Button) photo_upload_dialog.findViewById(R.id.ok_button_on_url);

                ok_button_on_url.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(url_edittext.getText().toString() != "")
                        {
                            // Get the url, dismiss the dialog and call new intent to list //
                            String photo_url = url_edittext.getText().toString();
                            photo_upload_dialog.dismiss();
                            tagger_from_url(photo_url, true);
                        }
                        else
                        {
                            Log.e("Error Url Empty", "You should enter a url !");
                        }
                    }
                });
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = photo_upload_dialog.getWindow();
                photo_upload_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                lp.copyFrom(window.getAttributes());
                //This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                photo_upload_dialog.show();
            }
        });



        // Showing the dialog //
        photo_dialog.show();
    }


    public void showUploadedPhotoDialog(Uri selected_image_uri)
    {
        // Creating dialog object //
        final Dialog uploaded_photo_dialog = new Dialog(this);
        uploaded_photo_dialog.setContentView(R.layout.photo_show_layout);

        // Getting the objects //
        final ImageView uploaded_photo = (ImageView) uploaded_photo_dialog.findViewById(R.id.image_uploaded);
        Button upload_button = (Button) uploaded_photo_dialog.findViewById(R.id.btn_upload);
        Button clear_button = (Button) uploaded_photo_dialog.findViewById(R.id.btn_clear);


        // Setting the image to the image view //
        uploaded_photo.setImageURI(selected_image_uri);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = uploaded_photo_dialog.getWindow();
        uploaded_photo_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        uploaded_photo_dialog.show();

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Tagger with upload //
                tagger_with_upload(uploaded_photo);
            }
        });

        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploaded_photo_dialog.dismiss();
            }
        });


    }

    public void showCapturedPhoto(Bitmap selected_image_bitmap)
    {
        // Creating dialog object //
        final Dialog uploaded_photo_dialog = new Dialog(this);
        uploaded_photo_dialog.setContentView(R.layout.photo_show_layout);

        // Getting the objects //
        final ImageView uploaded_photo = (ImageView) uploaded_photo_dialog.findViewById(R.id.image_uploaded);
        Button upload_button = (Button) uploaded_photo_dialog.findViewById(R.id.btn_upload);
        Button clear_button = (Button) uploaded_photo_dialog.findViewById(R.id.btn_clear);


        // Setting the image to the image view //
        uploaded_photo.setImageBitmap(selected_image_bitmap);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = uploaded_photo_dialog.getWindow();
        uploaded_photo_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        uploaded_photo_dialog.show();

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Tagger with upload //
                tagger_with_upload(uploaded_photo);
            }
        });

        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploaded_photo_dialog.dismiss();
            }
        });


    }




    public void tagger_with_upload(ImageView image_to_decode)
    {
        // HttpClient to get authorization to api //
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                        Credentials.basic(username, password));

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();
        // End of HttpClient //

        // MAKE IMAGGA CALL //
        // Retrofit Builder //
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.imagga.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        // End of Retrofit Builder //


        // Decoding ImageView to Base64 String to Upload //
        BitmapDrawable drawable = (BitmapDrawable) image_to_decode.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
        byte[] bb = bos.toByteArray();
        String encodedImage = Base64.encodeToString(bb, Base64.DEFAULT);
        // End of Decoding //




        // JsonPlaceHolderApi Object //
        final JsonPlaceHolderApi json_placeholder = retrofit.create(JsonPlaceHolderApi.class);
        // We'll call api to get the weather data //
        Call<ApiResponseImagga> call =
                json_placeholder.getUploadIdOfPhoto(encodedImage);

        // The network operations must be done in the background, but Retrofit has appropriate function. //
        // Enqueue function implements the background functionality, we don't need to create Async task //
        call.enqueue(new Callback<ApiResponseImagga>() {
            @Override
            public void onResponse(Call<ApiResponseImagga> call, Response<ApiResponseImagga> response) {
                // Means that our call was successful //
                Log.e("Successful", "Success !");
                String returned_id = response.body().getResult().getUploadId();
                tagger_from_url(returned_id, false);
            }

            @Override
            public void onFailure(Call<ApiResponseImagga> call, Throwable t) {
                Log.e("Failure", "Fail !");
                Log.e("Fail", "The Fail Is: " + t.toString());
                // TODO //
            }
        });


    }



    public void tagger_from_url(String image_url, boolean is_url)
    {
        // Http Client to get authorization //
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                        Credentials.basic(username, password));

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();

        // MAKE IMAGGA CALL //
        // Retrofit Builder //
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.imagga.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        // JsonPlaceHolderApi Object //
        final JsonPlaceHolderApi json_placeholder = retrofit.create(JsonPlaceHolderApi.class);
        Call<ApiResponseImagga> call;
        // Check if the parameter was a url or an image id /
        if(is_url)
        {
            // We'll call api to get the weather data //
            call = json_placeholder.getTagsOfPhoto(image_url);
        }
        else
        {
            call = json_placeholder.getTagsOfPhotoWithId(image_url);
        }


        // The network operations must be done in the background, but Retrofit has appropriate function. //
        // Enqueue function implements the background functionality, we don't need to create Async task //
        call.enqueue(new Callback<ApiResponseImagga>() {
            @Override
            public void onResponse(Call<ApiResponseImagga> call, Response<ApiResponseImagga> response) {
                // Means that our call was successful //
                Log.e("Successful", "Success !");
                // Getting recommended playlists aka returned json objects //
                ApiResponseImagga returned_tags = response.body();
                int i = 0; // Counter //
                // We iterate over the returned tags and get 5 highest of them //
                while(i < 5)
                {
                    tags[i] = returned_tags.getResult().getTags().get(i).getTag().getEn();
                    i++;
                }

                // Create a new intent to list recommended playlists //
                Intent list_intent = new Intent(MainScreenActivity.this, ListPlaylistsActivity.class);
                list_intent.putExtra("Keyword", tags[0]);
                startActivity(list_intent);
            }

            @Override
            public void onFailure(Call<ApiResponseImagga> call, Throwable t) {
                Log.e("Failure", "Fail !");
                // TODO //
            }
        });
    }

}

