package com.example.onurb.playlistrecommender.classes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Onurb on 15.12.2019.
 */

// ApiResponse class represents the api response itself. It contains different objects from different classes. //

public class ApiResponseWeather {

    // Serialized names corresponds to the actual field names of the api response. //
    // They may be different from the class names for the sake of simplicity. //
    @SerializedName("weather")
    private List<Weather> weather = null;

    @SerializedName("main")
    private WeatherValues main;

    @SerializedName("wind")
    private Wind wind;

    @SerializedName("name")
    private String name;

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public WeatherValues getMain() {
        return main;
    }

    public void setMain(WeatherValues main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}