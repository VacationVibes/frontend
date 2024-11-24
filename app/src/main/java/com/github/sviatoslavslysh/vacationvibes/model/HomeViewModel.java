package com.github.sviatoslavslysh.vacationvibes.model;

import androidx.lifecycle.ViewModel;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private List<Place> places;

    public boolean isDataLoaded() {
        return places == null || places.isEmpty();
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}
