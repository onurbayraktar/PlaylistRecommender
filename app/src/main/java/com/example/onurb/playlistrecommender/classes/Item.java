package com.example.onurb.playlistrecommender.classes;

/**
 * Created by Onurb on 15.12.2019.
 */


import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("description")
    private String description;

    @SerializedName("images")
    private List<Image> images = null;

    @SerializedName("name")
    private String name;

    @SerializedName("owner")
    private Owner owner;

    @SerializedName("tracks")
    private Track tracks;

    @SerializedName("uri")
    private String uri;

    @SerializedName("external_urls")
    private ExternalUrl external_url;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Track getTracks() {
        return tracks;
    }

    public void setTracks(Track tracks) {
        this.tracks = tracks;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ExternalUrl getExternal_url() {
        return external_url;
    }

    public void setExternal_url(ExternalUrl external_url) {
        this.external_url = external_url;
    }
}