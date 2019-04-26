package com.mobven.smartmovie.RestClient;

import com.mobven.smartmovie.Model.Movie;
import com.mobven.smartmovie.Model.MovieListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestInterface {

    @GET("{category}")
    Call<MovieListResponse> getMovieList(@Path("category") String category,
                                         @Query( "api_key" ) String api_key,
                                         @Query( "language" ) String language);



}
