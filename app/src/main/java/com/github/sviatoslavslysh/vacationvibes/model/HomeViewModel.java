package com.github.sviatoslavslysh.vacationvibes.model;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private List<Place> places = new ArrayList<>();
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
        if (places == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(places);
    }


    public int getPlacesAmount() {
        return places == null ? 0 : places.size();
    }

    // protected from accessing from different threads
    public synchronized void setPlaces(List<Place> places) {
        this.places = places;
    }

    public void addPlace(Place place) {
        places.add(place);
    }
}
