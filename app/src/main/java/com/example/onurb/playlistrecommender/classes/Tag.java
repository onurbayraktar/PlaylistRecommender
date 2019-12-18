package com.example.onurb.playlistrecommender.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Onurb on 15.12.2019.
 */

public class Tag {
    @SerializedName("confidence")

    private Double confidence;
    @SerializedName("tag")

    private Tag_ tag;

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Tag_ getTag() {
        return tag;
    }

    public void setTag(Tag_ tag) {
        this.tag = tag;
    }

}
