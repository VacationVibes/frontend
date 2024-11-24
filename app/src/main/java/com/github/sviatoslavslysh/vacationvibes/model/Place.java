package com.github.sviatoslavslysh.vacationvibes.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Place {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("types")
    private List<PlaceTypeMin> types;
    @SerializedName("images")
    private List<PlaceImageMin> images;
    @SerializedName("reactions")
    private List<PlaceReactionMin> reactions;

    public Place(String id,
                 String name,
                 String latitude,
                 String longitude,
                 String createdAt,
                 List<PlaceTypeMin> types,
                 List<PlaceImageMin> images,
                 List<PlaceReactionMin> reactions) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = createdAt;
        this.types = types;
        this.images = images;
        this.reactions = reactions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<PlaceTypeMin> getTypes() {
        return types;
    }

    public void setTypes(List<PlaceTypeMin> types) {
        this.types = types;
    }

    public List<PlaceImageMin> getImages() {
        return images;
    }

    public void setImages(List<PlaceImageMin> images) {
        this.images = images;
    }

    public List<PlaceReactionMin> getReactions() {
        return reactions;
    }

    public void setReactions(List<PlaceReactionMin> reactions) {
        this.reactions = reactions;
    }
}
