package com.github.sviatoslavslysh.vacationvibes.model;

import com.google.gson.annotations.SerializedName;

public class PlaceTypeMin {
    @SerializedName("type")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
