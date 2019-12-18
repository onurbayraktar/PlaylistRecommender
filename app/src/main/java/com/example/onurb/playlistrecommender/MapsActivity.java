package com.example.onurb.playlistrecommender;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onurb.playlistrecommender.classes.ApiResponseWeather;
import com.example.onurb.playlistrecommender.classes.JsonPlaceHolderApi;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng current_location;
    private String api_key = "652fc32a1ada039b3b4350a946ab3012";

    private Button correct_button, wrong_button;
    private TextView address_text_view;

    // Location manager and location listener variables //
    LocationManager location_manager;
    LocationListener location_listener;

    // ApiResponse object, that will be sent as parameter to spotifySearch() function //
    ApiResponseWeather current_weather_info;
    private String weather_desc;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Checking the permissions
        if(requestCode == 1)
        {
            // If the permission is granted //
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // If the app has ACCESS FINE LOCATION permission //
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    location_manager.requestLocationUpdates(location_manager.GPS_PROVIDER, 0, 0, location_listener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // Getting the buttons //
        correct_button = (Button) findViewById(R.id.button_correct);
        wrong_button = (Button) findViewById(R.id.button_wrong);
        address_text_view = (TextView) findViewById(R.id.address_text_view);

        // Retrofit Builder //
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // JsonPlaceHolderApi Object //
        final JsonPlaceHolderApi json_placeholder = retrofit.create(JsonPlaceHolderApi.class);

        // Setting OnClickListeners of our buttons //
        // onClickListener for correct button //
        correct_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // We'll call api to get the weather data //
                Call<ApiResponseWeather> call =
                        json_placeholder.getCurrentWeather(current_location.latitude,current_location.longitude,api_key);

                // The network operations must be done in the background, but Retrofit has appropriate function. //
                // Enqueue function implements the background functionality, we don't need to create Async task //
                call.enqueue(new Callback<ApiResponseWeather>() {
                    @Override
                    public void onResponse(Call<ApiResponseWeather> call, Response<ApiResponseWeather> response)
                    {
                        // Means that our call was successful //
                        Toast.makeText(MapsActivity.this, "Success", Toast.LENGTH_SHORT);
                        Log.e("Successful", "Success !");

                        // Assigning ApiResponse object to send it as parameter of spotifySearch() function. //
                        current_weather_info = response.body();
                        weather_desc = current_weather_info.getWeather().get(0).get_Desc_Short();

                        // To be more specific, we change the keywords a little bit //
                        switch (weather_desc){
                            case "Clouds":
                                weather_desc = "Cloudy";
                                break;
                            case "Clear":
                                weather_desc = "Sunny";
                                break;
                            case "Rain":
                                weather_desc = "Rainy";
                                break;
                            case "Snow":
                                weather_desc = "Snowy";
                                break;
                            default:
                                break;
                        }

                        // Calling new Intent to List the Playlists//
                        Intent list_intent = new Intent(MapsActivity.this, ListPlaylistsActivity.class);
                        list_intent.putExtra("Keyword", weather_desc);
                        startActivity(list_intent);
                    }

                    @Override
                    public void onFailure(Call<ApiResponseWeather> call, Throwable t)
                    {
                        Toast.makeText(MapsActivity.this, "Fail", Toast.LENGTH_SHORT);
                        Log.e("Failure", "Fail !");
                        // TODO //

                    }
                });
            }
        }); // End of onClickListener of correct button

        // onClickListener for wrong button //
        wrong_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chose_location_intent = new Intent(MapsActivity.this, ChoseLocationActivity.class);
                startActivity(chose_location_intent);
            }
        }); // End of onClickLister of wrong button //
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        location_manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Location my_location = location_manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        LatLng loc_lat_lng = new LatLng(my_location.getLatitude(),my_location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(loc_lat_lng).title("Your Location !"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc_lat_lng, 16.0f));
        setAddress(loc_lat_lng);


        // Location Listener functions //
        location_listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // In case of any location change, a marker will be added to the map //
                current_location = new LatLng(location.getLatitude(), location.getLongitude());

                // We will clear old marker //
                mMap.clear();

                // And add the new marker //
                mMap.addMarker(new MarkerOptions().position(current_location).title("Your Location !"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(current_location));
                setAddress(current_location);
            } // End of onLocationChanged

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        }; // End of location listener functions //

        if(Build.VERSION.SDK_INT < 23)
        {
            location_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, location_listener);
        }
        else
        {
            // If the app doesn't have location access permission, then we need to request it //
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            // If the app has location access permission, check the updates //
            else
            {
                // The line checks the location in every unit of time with location_listener //
                location_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, location_listener);

                //Location lastKnownLocation = location_manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                //current_location = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                // We will clear old marker //
                //mMap.clear();
                // And add the new marker //
                //mMap.addMarker(new MarkerOptions().position(current_location).title("Your Location !"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(current_location));
                //setAddress(current_location);

            }
        }

    } // End of onMapReady function



    private void setAddress(LatLng loc)
    {
        // Getting the textView //
        address_text_view = (TextView) findViewById(R.id.address_text_view_maps);
        // Reverse geocoding, getting the address of the user with selected location. //
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            // We will try to get addresses //
            List<android.location.Address> addresses_list = geocoder.getFromLocation(loc.latitude, loc.longitude, 1);
            if(addresses_list != null && addresses_list.size() > 0)
            {
                String address = addresses_list.get(0).getSubAdminArea() + ", " +
                        addresses_list.get(0).getAdminArea() + ", " +
                        addresses_list.get(0).getCountryCode();
                address_text_view.setText(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

} // End of MapsActivity
