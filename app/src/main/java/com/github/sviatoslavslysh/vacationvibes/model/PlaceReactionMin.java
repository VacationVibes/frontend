package com.github.sviatoslavslysh.vacationvibes.model;

import com.google.gson.annotations.SerializedName;

public class PlaceReactionMin {

    @SerializedName("reaction")
    private String reaction;
    @SerializedName("created_at")
    private String createdAt;

    public PlaceReactionMin(String reaction, String createdAt) {
        this.reaction = reaction;
        this.createdAt = createdAt;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
