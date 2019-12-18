package com.example.onurb.playlistrecommender.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Onurb on 18.12.2019.
 */

public class ExternalUrl {

    @SerializedName("spotify")
    private String spotifyURL;

    public String getSpotifyURL() {
        return spotifyURL;
    }

    public void setSpotifyURL(String spotifyURL) {
        this.spotifyURL = spotifyURL;
    }
}
