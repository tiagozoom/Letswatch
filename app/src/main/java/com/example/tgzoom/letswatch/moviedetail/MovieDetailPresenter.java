package com.example.tgzoom.letswatch.moviedetail;

import android.support.annotation.NonNull;

import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.Trailer;

import javax.inject.Inject;

/**
 * Created by tgzoom on 1/5/17.
 */

public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    private MovieDetailContract.View mMovieDetailView;

    @Inject
    MovieDetailPresenter(MovieDetailContract.View movieDetailView) {
        mMovieDetailView = movieDetailView;
    }

    @Override
    public void openTrailer(Trailer trailer) {

    }

    @Override
    public void markAsFavourite(@NonNull Movie movie) {

    }

    @Override
    public void unmarkAsFavourite(@NonNull int movieApiId) {

    }

    @Override
    public void start() {

    }
}
