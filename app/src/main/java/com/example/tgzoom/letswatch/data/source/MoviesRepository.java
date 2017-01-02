package com.example.tgzoom.letswatch.data.source;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tgzoom.letswatch.data.Movie;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;

/**
 * Created by tgzoom on 12/27/16.
 */
@Singleton
public class MoviesRepository implements MoviesDataSource {

    private final MoviesDataSource mMoviesRemoteDataSource;
    private final MoviesDataSource mMoviesLocalDataSource;
    private Observable<List<Integer>> mFavouriteMoviesIds;

    Map<String, Movie> mCachedMovies;

    @Inject
    MoviesRepository(@Remote MoviesDataSource moviesRemoteDataSource, @Local MoviesDataSource moviesLocalDataSource) {
        mMoviesLocalDataSource = moviesLocalDataSource;
        mMoviesRemoteDataSource = moviesRemoteDataSource;
        mFavouriteMoviesIds = getFavouriteMoviesIds();
    }

    @Override
    public Observable<List<Movie>> getMovies(String sort, int pageIndex) {
        Observable<List<Movie>> movies = mMoviesRemoteDataSource.getMovies(sort, pageIndex);

        return movies;
    }

    @Override
    public Observable<Movie> getMovie(@NonNull int movieApiId) {
        return null;
    }

    @Override
    public long markAsFavourite(@NonNull Movie movie) {
        return mMoviesLocalDataSource.markAsFavourite(movie);
    }

    @Override
    public void unmarkAsFavourite(@NonNull String movieApiId) {

    }

    @Override
    public void refreshMovies() {

    }

    @Override
    public int deleteAllMovies() {
        return 0;
    }

    @Override
    public Observable<List<Integer>> getFavouriteMoviesIds() {
        return mMoviesLocalDataSource.getFavouriteMoviesIds();
    }

    @Override
    public Observable<List<Movie>> getFavouriteMovies() {
        return mMoviesLocalDataSource.getFavouriteMovies();
    }
}
