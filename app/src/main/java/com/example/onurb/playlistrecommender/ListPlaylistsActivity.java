package com.example.onurb.playlistrecommender;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.onurb.playlistrecommender.classes.ApiResponseSpotify;
import com.example.onurb.playlistrecommender.classes.JsonPlaceHolderApi;
import com.google.android.gms.internal.act;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.io.InputStream;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListPlaylistsActivity extends AppCompatActivity {

    private ListView playlist_listview;

    private String[] playlist_names =
            {"Example","Example","Example","Example","Example","Example","Example","Example","Example","Example",
                    "Example","Example","Example","Example","Example","Example","Example","Example","Example","Example"};

    private String[] playlist_image_urls =
            {"Example","Example","Example","Example","Example","Example","Example","Example","Example","Example",
                    "Example","Example","Example","Example","Example","Example","Example","Example","Example","Example"};

    private String[] playlist_uris =
            {"Example","Example","Example","Example","Example","Example","Example","Example","Example","Example",
                    "Example","Example","Example","Example","Example","Example","Example","Example","Example","Example"};

    private String[] playlist_external_urls =
            {"Example","Example","Example","Example","Example","Example","Example","Example","Example","Example",
                    "Example","Example","Example","Example","Example","Example","Example","Example","Example","Example"};

    private String[] no_of_tracks_in_playlist =
            {"20","20","20","20","20","20","20","20","20","20","20","20","20","20","20","20","20","20","2","20"};

    private Boolean is_spotify_installed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_playlists);

        // First we need to check if the spotify app is installed on the device //
        is_spotify_installed = isAppInstalled(ListPlaylistsActivity.this, "com.spotify.music");
        fetchThePlaylists();
    } // End of onCreate method //

    // CustomAdapter class //
    class CustomAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.single_playlist, null);

            ImageView playlist_image = (ImageView)view.findViewById(R.id.playlist_imageView);
            TextView playlist_name = (TextView)view.findViewById(R.id.playlist_name);
            TextView playlist_no_of_tracks = (TextView)view.findViewById(R.id.playlist_no_of_songs);

            // Now we need to fill the values //
            String no_of_tracks = getString(R.string.no_of_tracks, no_of_tracks_in_playlist[i]);
            playlist_name.setText(playlist_names[i]);
            playlist_no_of_tracks.setText(no_of_tracks);
            new DownloadImageTask(playlist_image).execute(playlist_image_urls[i]);

            // Setting onClickListeners of each row //
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(is_spotify_installed)    // If spotify is installed, we start the app //
                    {
                        final Intent spotify_intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(playlist_uris[i]));   // Start with using internal uri //
                        startActivity(spotify_intent);
                    }
                    else                        // Else, spotify is not installed, we need to open a browser intent //
                    {
                        final Intent browser_intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(playlist_external_urls[i]));
                        startActivity(browser_intent);
                        Toast.makeText(ListPlaylistsActivity.this, "Not Installed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return view;
        }
    }



    private void fetchThePlaylists()
    {
        // Getting the intent and parameters //
        Intent current_intent = getIntent();
        String keyword = current_intent.getStringExtra("Keyword");  // Keyword will be passed to api call //
        String oauth_token = LoginActivity.spotify_token;
        //String oauth_token = msharedPreferences.getString("token", ""); // Oauth token will be passed to api call //

        // MAKE SPOTIFY CALL //
        // Retrofit Builder //
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // JsonPlaceHolderApi Object //
        final JsonPlaceHolderApi json_placeholder = retrofit.create(JsonPlaceHolderApi.class);
                // We'll call api to get the weather data //
                Call<ApiResponseSpotify> call =
                        json_placeholder.getRecommendedPlaylists(keyword, "playlist", "Authorization: Bearer " + oauth_token);

                // The network operations must be done in the background, but Retrofit has appropriate function. //
                // Enqueue function implements the background functionality, we don't need to create Async task //
                call.enqueue(new Callback<ApiResponseSpotify>() {
                    @Override
                    public void onResponse(Call<ApiResponseSpotify> call, Response<ApiResponseSpotify> response) {
                        // Means that our call was successful //
                        Log.e("Successful", "Success !");

                        // Getting recommended playlists aka returned json objects //
                        ApiResponseSpotify recommended_playlists = response.body();

                        // Assigning the necessary values to arrays //
                        // First, we define an iterator to iterate over the list //
                        Iterator iterator = recommended_playlists.getPlaylists().getItems().iterator();
                        int i = 0;
                        while (i < 20)  // Iterate over the list //
                        {
                            // Assigning values to the arrays //
                            playlist_names[i] = recommended_playlists.getPlaylists().getItems().get(i).getName();
                            no_of_tracks_in_playlist[i] = recommended_playlists.getPlaylists().getItems().get(i).getTracks().getTotal().toString();
                            playlist_image_urls[i] = recommended_playlists.getPlaylists().getItems().get(i).getImages().get(0).getUrl();
                            playlist_uris[i] = recommended_playlists.getPlaylists().getItems().get(i).getUri();
                            playlist_external_urls[i] = recommended_playlists.getPlaylists().getItems().get(i).getExternal_url().getSpotifyURL();
                            i++;
                        }

                        // Now, we completed to assign values to arrays, now we need to use adapter to fill the listview in onCreate method //
                        // Getting the view objects //
                        playlist_listview = (ListView) findViewById(R.id.playlist_list_view);

                        // Creating the adapter and set it to the listview //
                        CustomAdapter custom_adapter = new CustomAdapter();
                        playlist_listview.setAdapter(custom_adapter);
                    }

                    @Override
                    public void onFailure(Call<ApiResponseSpotify> call, Throwable t) {
                        Toast.makeText(ListPlaylistsActivity.this, "Fail", Toast.LENGTH_SHORT);
                        Log.e("Failure", "Fail !");
                        // TODO //

                    }
        });
    }



    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }



}