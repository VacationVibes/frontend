package com.github.sviatoslavslysh.vacationvibes.model;

import com.google.gson.annotations.SerializedName;

public class AddReactionRequest {
    @SerializedName("place_id")
    private String placeId;
    @SerializedName("reaction")
    private String reaction;

    public AddReactionRequest(String placeId, String reaction) {
        this.placeId = placeId;
        this.reaction = reaction;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }
}
