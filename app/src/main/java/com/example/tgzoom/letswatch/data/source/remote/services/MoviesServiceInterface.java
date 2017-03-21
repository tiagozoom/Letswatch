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

    @GET("3/movie/top_rated")
    Observable<Movie.Results> getTopRatedMovies(@Query("api_key") String api_key, @Query("page") int page);

    @GET("3/movie/popular")
    Observable<Movie.Results> getPopularMovies(@Query("api_key") String api_key, @Query("page") int page);

    @GET("3/movie/{movie_id}/videos")
    Observable<Trailer.Results> getTrailers(@Path("movie_id") int movie_id, @Query("api_key") String api_key);

    @GET("3/movie/search/movie/{query}")
    Observable<Movie.Results> searchMovies(@Path("query") String query,@Query("api_key") String api_key);
}
