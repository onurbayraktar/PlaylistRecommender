package com.example.onurb.playlistrecommender.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Onurb on 15.12.2019.
 */
// Weather Class represents the Weather field in the api call //
public class Weather {

    // Serialized names corresponds to the actual field names of the api response. //
    // They may be different from the class names for the sake of simplicity. //
    @SerializedName("id")
    private Integer id;

    @SerializedName("main")
    private String desc_short;

    @SerializedName("description")
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String get_Desc_Short() {
        return desc_short;
    }

    public void set_Desc_Short(String desc_short) {
        this.desc_short = desc_short;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}




