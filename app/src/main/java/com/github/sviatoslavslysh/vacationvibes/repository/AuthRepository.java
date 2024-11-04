package com.github.sviatoslavslysh.vacationvibes.repository;

import com.github.sviatoslavslysh.vacationvibes.api.ApiClient;
import com.github.sviatoslavslysh.vacationvibes.api.AuthApiService;
import com.github.sviatoslavslysh.vacationvibes.model.AuthToken;
import com.github.sviatoslavslysh.vacationvibes.model.ErrorResponse;
import com.github.sviatoslavslysh.vacationvibes.model.LoginRequest;
import com.github.sviatoslavslysh.vacationvibes.model.RegisterRequest;
import com.github.sviatoslavslysh.vacationvibes.model.User;
import com.github.sviatoslavslysh.vacationvibes.utils.AuthCallback;
import com.github.sviatoslavslysh.vacationvibes.utils.PreferencesManager;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.NonNull;

import java.io.IOException;

public class AuthRepository {

    private final AuthApiService authApiService;
    private final PreferencesManager preferencesManager;
    public AuthRepository(PreferencesManager preferencesManager) {
        this.authApiService = ApiClient.createService(AuthApiService.class);
        this.preferencesManager = preferencesManager;
    }

    public void login(String email, String password, final AuthCallback<AuthToken> callback) {
        LoginRequest loginRequest = new LoginRequest(email, password);

        authApiService.login(loginRequest).enqueue(new Callback<AuthToken>() {
            @Override
            public void onResponse(@NonNull Call<AuthToken> call, @NonNull Response<AuthToken> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthToken authToken = response.body();
                    saveToken(authToken);
                    callback.onSuccess(authToken);
                } else {
                    handleErrorResponse(response, callback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthToken> call, @NonNull Throwable t) {
                callback.onError("Login failed: " + t.getMessage());
            }
        });
    }

    public void register(String email, String password, String name, final AuthCallback<AuthToken> callback) {
        RegisterRequest registerRequest = new RegisterRequest(email, password, name);

        authApiService.register(registerRequest).enqueue(new Callback<AuthToken>() {
            @Override
            public void onResponse(@NonNull Call<AuthToken> call, @NonNull Response<AuthToken> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthToken authToken = response.body();
                    saveToken(authToken);
                    callback.onSuccess(authToken);
                } else {
                    handleErrorResponse(response, callback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthToken> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getCurrentUser(final AuthCallback<User> callback) {
        authApiService.getCurrentUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    handleErrorResponse(response, callback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    private <T> void handleErrorResponse(Response<T> response, AuthCallback<T> callback) {
        String errorMessage;

        switch (response.code()) {
            case 422:
                errorMessage = "Invalid input";
                break;
            case 500:
                errorMessage = "Server is down!";
                break;
            default:
                if (response.errorBody() == null) {
                    errorMessage = "Unknown error, code " + response.code();
                } else {
                    try {
                        String errorJson = response.errorBody().string();
                        ErrorResponse errorResponse = new Gson().fromJson(errorJson, ErrorResponse.class);
                        errorMessage = errorResponse.getDetail();
                    } catch (IOException e) {
                        errorMessage = "Failed to read error response: " + e.getMessage();
                    }
                }
                break;
        }
        callback.onError(errorMessage);
    }

    private void saveToken(AuthToken authToken) {
        preferencesManager.setToken(authToken.getTokenType() + " " + authToken.getToken());
    }

    public String getToken() {
        return preferencesManager.getToken();
    }
}
