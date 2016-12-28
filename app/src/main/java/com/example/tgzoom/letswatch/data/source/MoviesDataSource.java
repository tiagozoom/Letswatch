package com.example.tgzoom.letswatch.data.source;

import android.support.annotation.NonNull;

import com.example.tgzoom.letswatch.data.Movie;

import java.util.List;

import rx.Observable;

/**
 * Created by tgzoom on 12/27/16.
 */

public interface MoviesDataSource {
    Observable<List<Movie>> getMovies(String sort,int pageIndex);
    Observable<Movie> getMovie(@NonNull String movieApiId);
    Observable<List<Integer>> getFavouriteMoviesIds();
    void markAsFavourite(@NonNull Movie movie);
    void unmarkAsFavourite(@NonNull String movieApiId);
    void refreshMovies();
    void deleteAllMovies();
}
