package com.example.onurb.playlistrecommender.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Onurb on 16.12.2019.
 */

public class Upload {
    @SerializedName("upload_id")
    private String uploadId;

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
}
