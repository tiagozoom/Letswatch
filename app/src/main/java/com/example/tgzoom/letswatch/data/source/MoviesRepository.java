package com.example.tgzoom.letswatch.data.source;

import android.support.annotation.NonNull;

import com.example.tgzoom.letswatch.data.Movie;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by tgzoom on 12/27/16.
 */
@Singleton
public class MoviesRepository implements MoviesDataSource {

    private final MoviesDataSource mMoviesRemoteDataSource;
    private final MoviesDataSource mMoviesLocalDataSource;

    Map<String,Movie> mCachedMovies;

    @Inject
    MoviesRepository(@Remote MoviesDataSource moviesRemoteDataSource,@Local MoviesDataSource moviesLocalDataSource){
        mMoviesLocalDataSource = moviesLocalDataSource;
        mMoviesRemoteDataSource = moviesRemoteDataSource;
    }

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
