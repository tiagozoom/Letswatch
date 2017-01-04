package com.example.tgzoom.letswatch.movies;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.source.MoviesRepository;
import com.example.tgzoom.letswatch.util.schedulers.BaseScheduler;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tgzoom on 12/27/16.
 */

public class MoviesPresenter implements MoviesContract.Presenter{

    private final MoviesRepository mMoviesRepository;

    private final MoviesContract.View mMoviesView;

    private final BaseScheduler mScheduler;

    private CompositeSubscription mSubscriptions;

    @Inject
    MoviesPresenter(MoviesRepository moviesRepository, MoviesContract.View moviesView, BaseScheduler scheduler){
        mMoviesRepository = moviesRepository;
        mMoviesView = moviesView;
        mSubscriptions = new CompositeSubscription();
        mScheduler = scheduler;
    }

    @Inject
    void setupListeners() {
        mMoviesView.setPresenter(this);
    }

    @Override
    public void start() {
        loadMovies(false,1);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    public void loadMovies(@NonNull boolean forceUpdate, int currentPage){
        mSubscriptions.clear();
        mMoviesView.setLoadingIndicator(true);
        Subscription subscription = mMoviesRepository
                .getMovies("popularity.desc",currentPage)
                .observeOn(mScheduler.computation())
                .subscribeOn(mScheduler.ui())
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
}
