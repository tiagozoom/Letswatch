package com.example.tgzoom.letswatch.data.source;

import android.support.annotation.NonNull;

import com.example.tgzoom.letswatch.data.Movie;

import java.util.List;

/**
 * Created by tgzoom on 12/27/16.
 */

public interface MoviesDataSource {
    interface LoadMoviesCallback{
        void onMoviesLoaded(List<Movie> movies);
        void onDataNotAvailable();
    }

    interface GetMovieCallback{
        void onMovieLoaded(Movie movie);
        void onDataNotAvailable();
    }

    void getMovies(@NonNull LoadMoviesCallback callback);
    void getMovie(@NonNull String movieApiId,@NonNull GetMovieCallback callback);
    void markAsFavourite(@NonNull Movie movie);
    void unmarkAsFavourite(@NonNull String movieApiId);
    void refreshMovies();
    void deleteAllMovies();
}
