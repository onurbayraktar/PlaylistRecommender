package com.example.onurb.playlistrecommender.classes;

/**
 * Created by Onurb on 15.12.2019.
 */

import com.google.gson.annotations.SerializedName;

public class Owner {

    @SerializedName("display_name")
    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}