package com.example.onurb.playlistrecommender.classes;

/**
 * Created by Onurb on 15.12.2019.
 */

import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("url")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
