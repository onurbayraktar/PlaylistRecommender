package com.example.onurb.playlistrecommender.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Onurb on 15.12.2019.
 */

public class Wind {

    // Serialized names corresponds to the actual field names of the api response. //
    // They may be different from the class names for the sake of simplicity. //
    @SerializedName("speed")
    private Double speed;

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

}
