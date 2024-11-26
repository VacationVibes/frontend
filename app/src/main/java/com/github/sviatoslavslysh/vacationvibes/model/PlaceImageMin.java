package com.github.sviatoslavslysh.vacationvibes.model;

import com.google.gson.annotations.SerializedName;

public class PlaceImageMin {
    @SerializedName("image_url")
    private String imageUrl;

    public PlaceImageMin(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
