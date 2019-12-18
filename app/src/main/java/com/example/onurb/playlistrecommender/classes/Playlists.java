package com.example.onurb.playlistrecommender.classes;

/**
 * Created by Onurb on 15.12.2019.
 */
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Playlists {

    @SerializedName("items")
    private List<Item> items = null;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}