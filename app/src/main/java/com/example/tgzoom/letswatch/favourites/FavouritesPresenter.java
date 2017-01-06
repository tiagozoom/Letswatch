package com.example.tgzoom.letswatch.favourites;

import android.support.annotation.NonNull;

import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.source.MoviesRepository;
import com.example.tgzoom.letswatch.movies.MoviesContract;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by tgzoom on 12/27/16.
 */

public class FavouritesPresenter implements FavouritesContract.Presenter {

    private final MoviesRepository mMoviesRepository;

    private final FavouritesContract.View mFavouritesView;

    private CompositeSubscription mSubscriptions;

    @Inject
    public FavouritesPresenter(MoviesRepository moviesRepository, FavouritesContract.View favouritesView){
        mMoviesRepository = moviesRepository;
        mFavouritesView = favouritesView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadMovies(@NonNull boolean forceUpdate) {

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
