package com.example.tgzoom.letswatch.data.source;

import android.support.annotation.NonNull;

import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.Trailer;

import java.util.List;

import rx.Observable;

/**
 * Created by tgzoom on 12/27/16.
 */

public interface MoviesDataSource {
    Observable<List<Movie>> getMovies(String sort,int pageIndex);
    Observable<List<Movie>> getFavouriteMovies();
    Observable<Movie> getMovie(@NonNull int movieApiId);
    Observable<List<Integer>> getFavouriteMoviesIds();
    Observable<List<Trailer>> getTrailers(int movieApiId);

    long markAsFavourite(@NonNull Movie movie);
    void unmarkAsFavourite(@NonNull int movieApiId);
    void refreshMovies();
    int deleteAllMovies();
}
