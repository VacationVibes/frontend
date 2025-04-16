package com.github.sviatoslavslysh.vacationvibes.model;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

public class Comment {
    @SerializedName("id")
    private String id;

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("user")
    private User user;

    @SerializedName("comment")
    private String comment;

    @SerializedName("rating")
    private Double rating;

    @SerializedName("created_at")
    private String createdAt;

    // Constructor
    public Comment(String id, String placeId, User user, String comment, Double rating, String createdAt) {
        this.id = id;
        this.placeId = placeId;
        this.user = user;
        this.comment = comment;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    // Getter and Setter for 'id'
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and Setter for 'placeId'
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    // Getter and Setter for 'user'
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Getter and Setter for 'comment'
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // Getter and Setter for 'rating'
    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
