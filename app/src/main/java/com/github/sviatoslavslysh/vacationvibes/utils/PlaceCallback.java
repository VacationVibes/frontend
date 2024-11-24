package com.github.sviatoslavslysh.vacationvibes.utils;

import com.github.sviatoslavslysh.vacationvibes.model.Place;

import java.util.List;

public interface PlaceCallback<T> {
    void onSuccess(T result);
    void onError(String error);
    void onInvalidAuth();
}
