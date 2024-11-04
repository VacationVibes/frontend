package com.github.sviatoslavslysh.vacationvibes.utils;

public interface AuthCallback<T> {
    void onSuccess(T result);
    void onError(String error);
}
