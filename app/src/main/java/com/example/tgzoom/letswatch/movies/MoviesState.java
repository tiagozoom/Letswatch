package com.example.tgzoom.letswatch.movies;

import android.os.Parcelable;

import com.example.tgzoom.letswatch.BaseState;
import com.example.tgzoom.letswatch.data.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgzoom on 1/3/17.
 */

public class MoviesState implements MoviesContract.State {
    private final int mCurrentPage;
    private final List<Movie> mMovies;

    public MoviesState(){
        mCurrentPage = 1;
        mMovies = new ArrayList<>();
    }

    @Override
    public ArrayList<Movie> getMovies() {
        return (ArrayList<Movie>) mMovies;
    }

    @Override
    public int getCurrentPage() {
        return mCurrentPage;
    }
}
