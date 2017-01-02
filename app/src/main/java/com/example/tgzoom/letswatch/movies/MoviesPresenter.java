package com.example.tgzoom.letswatch.movies;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.source.MoviesRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tgzoom on 12/27/16.
 */

public class MoviesPresenter implements MoviesContract.Presenter{
    private MoviesRepository mMoviesRepository;
    private MoviesContract.View mMoviesView;
    private CompositeSubscription mSubscriptions;

    @Inject
    MoviesPresenter(MoviesRepository moviesRepository, MoviesContract.View moviesView){
        mMoviesRepository = moviesRepository;
        mMoviesView = moviesView;
        mSubscriptions = new CompositeSubscription();
    }

    @Inject
    void setupListeners() {
        mMoviesView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadMovies(@NonNull boolean forceUpdate, int currentPage) {
        mSubscriptions.clear();
        Subscription subscription = mMoviesRepository
                .getMovies("popularity.desc",currentPage)
                .subscribe(
                        new Observer<List<Movie>>() {
                            @Override
                            public void onCompleted() {
                                mMoviesView.setLoadingIndicator(false);
                            }

                            @Override
                            public void onError(Throwable e) {
                                mMoviesView.showLoadingMoviesError();
                            }

                            @Override
                            public void onNext(List<Movie> movies) {
                                processMovies(movies);
                            }
                        }

                );

        mSubscriptions.add(subscription);
    }

    private void processMovies(@NonNull List<Movie> movies){
        mMoviesView.showMovies(movies);
    }

    @Override
    public void markAsFavourite(@NonNull Movie movie) {

    }

    @Override
    public void unmarkAsFavourite(@NonNull int movieApiId) {

    }

    @Override
    public void start() {
        loadMovies(false,1);
    }
}
