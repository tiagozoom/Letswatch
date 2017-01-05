package com.example.tgzoom.letswatch.moviedetail;

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
    public void start() {

    }
}
