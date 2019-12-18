package com.example.onurb.playlistrecommender.classes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Onurb on 15.12.2019.
 */

public class Result {

    @SerializedName("tags")
    private List<Tag> tags = null;

    @SerializedName("upload_id")
    private String uploadId;

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
