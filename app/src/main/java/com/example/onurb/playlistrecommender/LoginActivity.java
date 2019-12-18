package com.example.onurb.playlistrecommender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

// This Activity is used to log the user in to the Spotify Account //
// It is required to use the Spotify Api //

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;
    private SharedPreferences msharedPreferences;
    private RequestQueue queue;

    // Spotify Developer Keys //
    private static final String CLIENT_ID = "f7dc30a179e642a19484b2953ddefcd8";
    private static final String REDIRECT_URI = "com.example.onurb.playlistrecommender://callback";
    private static final int REQUEST_CODE = 1337;
    private static final String SCOPES = "user-read-recently-played,user-library-modify,user-read-email,user-read-private";
    //private static final String SCOPES = "user-read-email,user-read-private";
    public static String spotify_token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Spotify API Calls //
        authenticateSpotify();

        msharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);
    }


    private void authenticateSpotify() {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{SCOPES});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    Log.d("STARTING", "GOT AUTH TOKEN");
                    Log.d("CODE", "TOKEN IS: " + msharedPreferences.getString("token", ""));
                    editor.apply();

                    // Since we got the Oauth Token, now we can iterate through the other Intents //
                    spotify_token = msharedPreferences.getString("token", "");  // Set the token //
                    Intent main_screen_intent = new Intent(LoginActivity.this, MainScreenActivity.class);
                    startActivity(main_screen_intent);
                    break;

                // Auth flow returned an error
                case ERROR:
                    Log.e("ERROR", "There was an error");
                    Log.e("WHAT", "" + response.getError());
                    Log.e("WHAT1", "" + response.getState());
                    Log.e("WHAT2", "" + response.getCode());

                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    Log.e("CANCEL", "The flow was cancelled");
                    // Handle other cases
            }
        }
    }
}
