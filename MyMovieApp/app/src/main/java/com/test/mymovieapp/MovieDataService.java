package com.test.mymovieapp;

import com.test.mymovieapp.model.Movie;
import com.test.mymovieapp.model.Movie_Results;
import com.test.mymovieapp.model.Reviews_Results;
import com.test.mymovieapp.model.Trailer_Response;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDataService {

    @GET("movie/top_rated")
    Call<Movie_Results> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/popular")
    Call<Movie_Results> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<Reviews_Results> getMovieReviews(@Path("movie_id") int movieId, @Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{movie_id}/videos")
    Call<Trailer_Response> getMovieTrailers(@Path("movie_id") int movieId, @Query("api_key") String apiKey);
}
