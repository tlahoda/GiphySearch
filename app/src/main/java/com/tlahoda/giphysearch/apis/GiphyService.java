package com.tlahoda.giphysearch.apis;

import com.tlahoda.giphysearch.models.GiphySearchResponse;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GiphyService {
    @GET("/v1/gifs/search?fmt=json")
    Call<GiphySearchResponse> getSearchResults(@NonNull @Query("q") String query, @Query("limit") int limit, @Query("offset") int offset, @NonNull @Query("api_key") String apiKey);
}
