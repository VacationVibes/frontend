package com.github.sviatoslavslysh.vacationvibes.api;

import com.github.sviatoslavslysh.vacationvibes.model.AddCommentRequest;
import com.github.sviatoslavslysh.vacationvibes.model.AddReactionRequest;
import com.github.sviatoslavslysh.vacationvibes.model.AddReactionResponse;
import com.github.sviatoslavslysh.vacationvibes.model.Comment;
import com.github.sviatoslavslysh.vacationvibes.model.Place;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PlaceApiService {
    @POST("place/reaction")
    Call<AddReactionResponse> addReaction(@Body AddReactionRequest request);

    @GET("place/reactions")
    Call<List<Place>> getReactions(@Query("offset") int offset, @Query("limit") int limit);

    @GET("place/feed")
    Call<List<Place>> getFeed(@Query("ignore_ids") List<String> ignoreIds);

    @GET("place/comments")
    Call<List<Comment>> getComments(@Query("place_id") String placeId);

    @POST("place/comment")
    Call<Void> addComment(@Body AddCommentRequest request);
}
