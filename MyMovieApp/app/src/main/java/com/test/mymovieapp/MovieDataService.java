package com.test.mymovieapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieDataService {

    @GET("movie/popular")
    Call<API_Results> getPopularMovies(@Query("page") int page, @Query("api_key")String apiKey);

    @GET("movie/top_rated")
    Call<API_Results> getTopRatedMovies(@Query("page") int page, @Query("api_key")String apiKey);

}
