package com.example.tgzoom.letswatch.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.source.MoviesDataSource;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by tgzoom on 12/27/16.
 */

public class MoviesLocalDataSource implements MoviesDataSource {

    private MoviesDbHelper mMoviesDbHelper;

    public MoviesLocalDataSource(@NonNull Context context){
        mMoviesDbHelper = new MoviesDbHelper(context);
    }

    @Override
    public Observable<List<Movie>> getMovies(String sort, int pageIndex) {
        List<Movie> movies = new ArrayList<>();
        SQLiteDatabase db = mMoviesDbHelper.getReadableDatabase();

        return null;
    }

    @Override
    public Observable<Movie> getMovie(@NonNull String movieApiId) {
        return null;
    }

    @Override
    public Observable<List<Integer>> getFavouriteMoviesIds() {
        return null;
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
