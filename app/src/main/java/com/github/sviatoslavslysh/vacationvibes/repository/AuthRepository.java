package com.github.sviatoslavslysh.vacationvibes.repository;

import android.content.Context;

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
import retrofit2.Response;

import androidx.annotation.NonNull;
import retrofit2.Callback;
import java.io.IOException;

public class AuthRepository {

    private AuthApiService authApiService;
    private final PreferencesManager preferencesManager;

    public AuthRepository(PreferencesManager preferencesManager, Context context) {
        this.authApiService = ApiClient.createService(AuthApiService.class, context);
        this.preferencesManager = preferencesManager;
    }


    public void login(String email, String password, final AuthCallback<AuthToken> authCallback) {
        LoginRequest loginRequest = new LoginRequest(email, password);

        authApiService.login(loginRequest).enqueue(new Callback<AuthToken>() {
            @Override
            public void onResponse(@NonNull Call<AuthToken> call, @NonNull Response<AuthToken> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthToken authToken = response.body();
                    authCallback.onSuccess(authToken);
                } else {
                    handleErrorResponse(response, authCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthToken> call, @NonNull Throwable t) {
                authCallback.onError("Login failed: " + t.getMessage());
            }
        });
    }

    public void register(String email, String password, String name, final AuthCallback<AuthToken> authCallback) {
        RegisterRequest registerRequest = new RegisterRequest(email, password, name);

        authApiService.register(registerRequest).enqueue(new Callback<AuthToken>() {
            @Override
            public void onResponse(@NonNull Call<AuthToken> call, @NonNull Response<AuthToken> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthToken authToken = response.body();
                    System.out.println(authToken);
                    authCallback.onSuccess(authToken);
                } else {
                    handleErrorResponse(response, authCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthToken> call, @NonNull Throwable t) {
                authCallback.onError(t.getMessage());
            }
        });
    }

    public void getCurrentUser(final AuthCallback<User> authCallback) {
        authApiService.getCurrentUser().enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    authCallback.onSuccess(response.body());
                } else {
                    handleErrorResponse(response, authCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                authCallback.onError(t.getMessage());
            }
        });
    }

    private <T> void handleErrorResponse(Response<T> response, AuthCallback<T> authCallback) {
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
        authCallback.onError(errorMessage);
    }
}
