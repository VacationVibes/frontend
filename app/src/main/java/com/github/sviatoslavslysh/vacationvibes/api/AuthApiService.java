package com.github.sviatoslavslysh.vacationvibes.api;

import com.github.sviatoslavslysh.vacationvibes.model.AuthToken;
import com.github.sviatoslavslysh.vacationvibes.model.LoginRequest;
import com.github.sviatoslavslysh.vacationvibes.model.RegisterRequest;
import com.github.sviatoslavslysh.vacationvibes.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthApiService {

    @POST("auth/login")
    Call<AuthToken> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<AuthToken> register(@Body RegisterRequest request);

    @GET("auth/me")
    Call<User> getCurrentUser();
}
