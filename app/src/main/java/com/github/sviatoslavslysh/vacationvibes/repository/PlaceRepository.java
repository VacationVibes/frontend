package com.github.sviatoslavslysh.vacationvibes.repository;

import android.content.Context;

import com.github.sviatoslavslysh.vacationvibes.api.ApiClient;
import com.github.sviatoslavslysh.vacationvibes.api.PlaceApiService;
import com.github.sviatoslavslysh.vacationvibes.model.AddCommentRequest;
import com.github.sviatoslavslysh.vacationvibes.model.AddReactionRequest;
import com.github.sviatoslavslysh.vacationvibes.model.AddReactionResponse;
import com.github.sviatoslavslysh.vacationvibes.model.Comment;
import com.github.sviatoslavslysh.vacationvibes.model.ErrorResponse;
import com.github.sviatoslavslysh.vacationvibes.model.Place;
import com.github.sviatoslavslysh.vacationvibes.utils.PlaceCallback;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class PlaceRepository {

    private final PlaceApiService placeApiService;

    public PlaceRepository(Context context) {
        this.placeApiService = ApiClient.createService(PlaceApiService.class, context);
    }

    public void addReaction(String placeId, String reaction, final PlaceCallback<AddReactionResponse> placeCallback) {
        AddReactionRequest addReactionRequest = new AddReactionRequest(placeId, reaction);

        placeApiService.addReaction(addReactionRequest).enqueue(new Callback<AddReactionResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddReactionResponse> call, @NonNull Response<AddReactionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AddReactionResponse addReactionResponse = response.body();
                    placeCallback.onSuccess(addReactionResponse);
                } else if (response.code() == 401 || response.code() == 400) {
                    String errorJson = null;
                    try {
                        assert response.errorBody() != null;
                        errorJson = response.errorBody().string();
                        ErrorResponse errorResponse = new Gson().fromJson(errorJson, ErrorResponse.class);
                        String errorMessage = errorResponse.getDetail();
                        if (Objects.equals(errorMessage, "Not authenticated") || Objects.equals(errorMessage, "Invalid credentials")) {
                            placeCallback.onInvalidAuth();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    handleErrorResponse(response, placeCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddReactionResponse> call, @NonNull Throwable t) {
                placeCallback.onError("Login failed: " + t.getMessage());
            }
        });
    }

    public void getReactions(Integer offset, Integer limit, final PlaceCallback<List<Place>> placeCallback) {
        placeApiService.getReactions(offset, limit).enqueue(new retrofit2.Callback<List<Place>>() {
            @Override
            public void onResponse(@NonNull Call<List<Place>> call, @NonNull Response<List<Place>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Place> places = response.body();
                    placeCallback.onSuccess(places);
                } else if (response.code() == 401 || response.code() == 400) {
                    String errorJson = null;
                    try {
                        assert response.errorBody() != null;
                        errorJson = response.errorBody().string();
                        ErrorResponse errorResponse = new Gson().fromJson(errorJson, ErrorResponse.class);
                        String errorMessage = errorResponse.getDetail();
                        if (Objects.equals(errorMessage, "Not authenticated") || Objects.equals(errorMessage, "Invalid credentials")) {
                            placeCallback.onInvalidAuth();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    handleErrorResponse(response, placeCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Place>> call, @NonNull Throwable t) {
                placeCallback.onError(t.getMessage());
            }
        });
    }

    public void getFeed(List<String> ignoreIds, final PlaceCallback<List<Place>> placeCallback) {
        placeApiService.getFeed(ignoreIds).enqueue(new retrofit2.Callback<List<Place>>() {
            @Override
            public void onResponse(@NonNull Call<List<Place>> call, @NonNull Response<List<Place>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Place> places = response.body();
                    placeCallback.onSuccess(places);
                } else if (response.code() == 401 || response.code() == 400) {
                    String errorJson = null;
                    try {
                        assert response.errorBody() != null;
                        errorJson = response.errorBody().string();
                        ErrorResponse errorResponse = new Gson().fromJson(errorJson, ErrorResponse.class);
                        String errorMessage = errorResponse.getDetail();
                        if (Objects.equals(errorMessage, "Not authenticated") || Objects.equals(errorMessage, "Invalid credentials")) {
                            placeCallback.onInvalidAuth();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    handleErrorResponse(response, placeCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Place>> call, @NonNull Throwable t) {
                placeCallback.onError(t.getMessage());
            }
        });
    }

    public void getComments(String placeId, final PlaceCallback<List<Comment>> placeCallback) {
        placeApiService.getComments(placeId).enqueue(new retrofit2.Callback<List<Comment>>() {
            @Override
            public void onResponse(@NonNull Call<List<Comment>> call, @NonNull Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Comment> comments = response.body();
                    placeCallback.onSuccess(comments);
                } else if (response.code() == 401 || response.code() == 400) {
                    String errorJson = null;
                    try {
                        assert response.errorBody() != null;
                        errorJson = response.errorBody().string();
                        ErrorResponse errorResponse = new Gson().fromJson(errorJson, ErrorResponse.class);
                        String errorMessage = errorResponse.getDetail();
                        if (Objects.equals(errorMessage, "Not authenticated") || Objects.equals(errorMessage, "Invalid credentials")) {
                            placeCallback.onInvalidAuth();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    handleErrorResponse(response, placeCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Comment>> call, @NonNull Throwable t) {
                placeCallback.onError(t.getMessage());
            }
        });
    }

    public void addComment(AddCommentRequest request, final PlaceCallback<Void> placeCallback) {
        placeApiService.addComment(request).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    placeCallback.onSuccess(null);
                } else if (response.code() == 401 || response.code() == 400) {
                    String errorJson = null;
                    try {
                        assert response.errorBody() != null;
                        errorJson = response.errorBody().string();
                        ErrorResponse errorResponse = new Gson().fromJson(errorJson, ErrorResponse.class);
                        String errorMessage = errorResponse.getDetail();
                        if (Objects.equals(errorMessage, "Not authenticated") || Objects.equals(errorMessage, "Invalid credentials")) {
                            placeCallback.onInvalidAuth();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    handleErrorResponse(response, placeCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                placeCallback.onError(t.getMessage());
            }
        });
    }

    private <T> void handleErrorResponse(Response<T> response, PlaceCallback<T> placeCallback) {
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
        placeCallback.onError(errorMessage);
    }
}
