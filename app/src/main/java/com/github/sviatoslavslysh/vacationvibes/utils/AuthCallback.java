package com.github.sviatoslavslysh.vacationvibes.utils;

import com.github.sviatoslavslysh.vacationvibes.model.AuthToken;

public interface AuthCallback<T> {
    void onSuccess(T result);
    void onError(String error);
}
