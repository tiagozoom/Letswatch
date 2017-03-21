package com.example.tgzoom.letswatch.data.source.remote.services;

import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.Trailer;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by tgzoom on 12/28/16.
 */

public interface MoviesServiceInterface {
    @GET("3/discover/movie")
    Observable<Movie.Results> getMovies(@Query("sort_by") String sort, @Query("page") int page, @Query("api_key") String api_key);

    @GET("3/movie/{movie_id}/videos")
    Observable<Trailer.Results> getTrailers(@Path("movie_id") int movie_id, @Query("api_key") String api_key);

    @GET("3/search/movie")
    Observable<Movie.Results> searchMovies(@Query("query") String query,@Query("page") int page,@Query("api_key") String api_key);
}
