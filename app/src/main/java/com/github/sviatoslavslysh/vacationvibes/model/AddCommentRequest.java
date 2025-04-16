package com.github.sviatoslavslysh.vacationvibes.model;

import com.google.gson.annotations.SerializedName;

public class AddCommentRequest {
    @SerializedName("place_id")
    private String placeId;

    @SerializedName("comment")
    private String comment;

    @SerializedName("rating")
    private Double rating;

    public AddCommentRequest(String placeId, String comment, Double rating) {
        this.placeId = placeId;
        this.comment = comment;
        this.rating = rating;
    }
}
