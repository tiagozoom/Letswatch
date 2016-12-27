package com.example.tgzoom.letswatch.data.source.remote;

import android.support.annotation.NonNull;

import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.source.MoviesDataSource;
import com.example.tgzoom.letswatch.data.source.Remote;

/**
 * Created by tgzoom on 12/27/16.
 */

public class MoviesRemoteDataSource implements MoviesDataSource{
    @Override
    public void getMovies(@NonNull LoadMoviesCallback callback) {

    }

    @Override
    public void getMovie(@NonNull String movieApiId, @NonNull GetMovieCallback callback) {

    }

    @Override
    public void markAsFavourite(@NonNull Movie movie) {

    }

    @Override
    public void unmarkAsFavourite(@NonNull String movieApiId) {

    }

    @Override
    public void refreshMovies() {

    }

    @Override
    public void deleteAllMovies() {

    }
}
