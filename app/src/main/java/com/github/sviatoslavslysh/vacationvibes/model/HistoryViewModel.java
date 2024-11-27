package com.github.sviatoslavslysh.vacationvibes.model;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryViewModel extends ViewModel {
    private List<Place> places = new ArrayList<>();
    private int offset;
    private int limit = 40;
    private boolean awaitingResponse;
    private long lastResponseTime;

    public boolean isAwaitingResponse() {
        if (awaitingResponse) {
            return true;
        } else if (System.currentTimeMillis() - lastResponseTime < 3000) {
            return true;
        } else {
            return false;
        }
    }

    public void setAwaitingResponse(boolean awaitingResponse) {
        this.awaitingResponse = awaitingResponse;
        if (awaitingResponse) {
            lastResponseTime = System.currentTimeMillis();
        }
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
    public void addPlace(Place place) {
        places.add(place);
    }

    public List<Place> getPlaces() {
        return new ArrayList<>(places);
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getOffset() {
        return places.size(); // - places.size() % limit;
    }

    public void clean() {
        places = new ArrayList<>();
    }
}
