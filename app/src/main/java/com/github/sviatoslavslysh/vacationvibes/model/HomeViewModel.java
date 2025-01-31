package com.github.sviatoslavslysh.vacationvibes.model;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private List<Place> places;
    private boolean awaitingResponse;

    public boolean isAwaitingResponse() {
        return awaitingResponse;
    }

    public void setAwaitingResponse(boolean awaitingResponse) {
        this.awaitingResponse = awaitingResponse;
    }

    public boolean isDataLoaded() {
        return places != null && !places.isEmpty();
    }

    public List<Place> getPlaces() {
        return new ArrayList<>(places);
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}
