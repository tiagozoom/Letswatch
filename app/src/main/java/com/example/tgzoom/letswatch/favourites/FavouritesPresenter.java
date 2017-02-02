package com.example.tgzoom.letswatch.favourites;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tgzoom.letswatch.BaseState;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.source.MoviesRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tgzoom on 12/27/16.
 */

public class FavouritesPresenter implements FavouritesContract.Presenter {

    private final MoviesRepository mMoviesRepository;
    private final FavouritesContract.View mFavouritesView;
    private CompositeSubscription mSubscriptions  = new CompositeSubscription();

    @Inject
    FavouritesPresenter(MoviesRepository moviesRepository, FavouritesContract.View favouritesView){
        mMoviesRepository = moviesRepository;
        mFavouritesView = favouritesView;
    }

    @Override
    public void loadMovies(@NonNull boolean showLoadingBar) {
        mSubscriptions.clear();
        mFavouritesView.setLoadingIndicator(true);
        Subscription subscription = mMoviesRepository
                .getFavouriteMovies()
                .subscribe(
                        new Observer<List<Movie>>() {
                            @Override
                            public void onCompleted() {
                                mFavouritesView.setLoadingIndicator(false);
                            }

                            @Override
                            public void onError(Throwable e) {
                                mFavouritesView.showLoadingMoviesError();
                            }

                            @Override
                            public void onNext(List<Movie> movies) {
                                mFavouritesView.hideRefresh();
                                processMovies(movies);
                            }
                        }
                );

        mSubscriptions.add(subscription);
    }

    private void processMovies(@NonNull List<Movie> movies) {
        mFavouritesView.showMovies(movies);
    }

    @Override
    public void markAsFavourite(@NonNull Movie movie) {
        mMoviesRepository.markAsFavourite(movie);
        mFavouritesView.showMarkedAsFavouriteMessage();
    }

    @Override
    public void unmarkAsFavourite(@NonNull int movieApiId) {
        mMoviesRepository.unmarkAsFavourite(movieApiId);
        mFavouritesView.showUnmarkedAsFavouriteMessage();
    }

    @Override
    public void openDetails(Movie movie) {
        mFavouritesView.showMovieDetails(movie);
    }

    @Override
    public void testConnectivity() {

    }

    private boolean hasConnectivity() {
        return false;
    }

    @Override
    public void start(boolean showLoadingBar) {
        loadMovies(showLoadingBar);
    }
}
