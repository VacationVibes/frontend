package com.github.sviatoslavslysh.vacationvibes.model;
import androidx.lifecycle.ViewModel;


public class ProfileViewModel extends ViewModel {
    private String name;
    private String email;

    private String id;
    private User user;
    private boolean awaitingResponse;
    public boolean isAwaitingResponse() {
        return awaitingResponse;
    }

    public void setAwaitingResponse(boolean awaitingResponse) {
        this.awaitingResponse = awaitingResponse;
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
