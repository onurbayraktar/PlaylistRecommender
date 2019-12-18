package com.example.onurb.playlistrecommender.classes;

/**
 * Created by Onurb on 15.12.2019.
 */
import com.google.gson.annotations.SerializedName;

public class Track {

    @SerializedName("total")
    private Integer total;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}