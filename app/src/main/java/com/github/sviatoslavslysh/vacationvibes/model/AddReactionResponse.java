package com.github.sviatoslavslysh.vacationvibes.model;

import com.google.gson.annotations.SerializedName;

public class AddReactionResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
