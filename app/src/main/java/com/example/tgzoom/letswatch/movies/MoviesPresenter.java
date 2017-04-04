package com.example.tgzoom.letswatch.movies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.MovieList;
import com.example.tgzoom.letswatch.data.source.MoviesRepository;
import com.example.tgzoom.letswatch.favourites.FavouriteObservableImp;
import com.example.tgzoom.letswatch.util.PreferencesUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tgzoom on 12/27/16.
 */

public class MoviesPresenter implements MoviesContract.Presenter {

    private final MoviesRepository mMoviesRepository;
    private final MoviesContract.View mMoviesView;
    private final Context mContext;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private ConnectivityManager mConnectivityManager;
    private Observable<List<Integer>> mFavouriteMoviesIds;

    @Inject
    MoviesPresenter(MoviesRepository moviesRepository, MoviesContract.View moviesView, Context context, ConnectivityManager connectivityManager) {
        mMoviesRepository = moviesRepository;
        mMoviesView = moviesView;
        mFavouriteMoviesIds = mMoviesRepository.getFavouriteMoviesIds();
        mContext = context;
        mConnectivityManager = connectivityManager;
        mSubscriptions.add(mMoviesRepository.getFavouriteClickEvent().subscribe(
                new Action1<FavouriteObservableImp.FavouriteClickEvent>() {
                    @Override
                    public void call(FavouriteObservableImp.FavouriteClickEvent favouriteClickEvent) {
                        mMoviesView.updateMovies(favouriteClickEvent.movieApiId, favouriteClickEvent.isFavorite);
                    }
                })
        );
    }

    @Inject
    void setupListeners() {
        mMoviesView.setPresenter(this);
    }

    @Override
    public void start(boolean showLoadingBar) {
        loadMovies(1, showLoadingBar);
    }

    public void loadMovies(int currentPage, final boolean showLoadingBar) {
        mMoviesView.setLoadingIndicator(true);
        if (showLoadingBar) {
            mMoviesView.showLoadingBar();
        }

        Subscription subscription1 = mMoviesRepository
                .getMovieList(PreferencesUtils.getPreferredSortOrder(mContext), currentPage)
                .withLatestFrom(mFavouriteMoviesIds, mMoviesRepository.getFavouriteMoviesIdsMapper())
                .retry()
                .subscribe(
                        new Observer<MovieList>() {
                            @Override
                            public void onCompleted() {
                                mMoviesView.setLoadingIndicator(false);
                                mMoviesView.hideLoadingBar();
                                mMoviesView.hideRefresh();
                            }

                            @Override
                            public void onError(Throwable e) {
                                mMoviesView.hideLoadingBar();
                                mMoviesView.hideRefresh();
                                mMoviesView.showLoadingMoviesError();
                            }

                            @Override
                            public void onNext(MovieList movieList) {
                                processMovies(movieList);
                            }
                        }
                );
        mSubscriptions.add(subscription1);
    }

    private void processMovies(@NonNull MovieList movieList) {
        List<Movie> movies = movieList.getMovies();
        mMoviesView.showMovies(movies);
        if(movieList.endOfList()) {
            mMoviesView.setEndOfList();
        }
    }

    @Override
    public void markAsFavourite(@NonNull Movie movie) {
        mMoviesRepository.markAsFavourite(movie);
        mMoviesView.showMarkedAsFavouriteMessage();
    }

    @Override
    public void unmarkAsFavourite(@NonNull int movieApiId) {
        mMoviesRepository.unmarkAsFavourite(movieApiId);
        mMoviesView.showUnmarkedAsFavouriteMessage();
    }

    @Override
    public void openDetails(Movie movie) {
        mMoviesView.showMovieDetails(movie);
    }


    //TODO Make sure that this is the best place to signalize that the current state of the loading proccess is false
    @Override
    public void testConnectivity() {
        if (hasConnectivity()) {
            mMoviesView.hideMessage();
        } else {
            mMoviesView.setLoadingIndicator(false);
            mMoviesView.showNoConnectivityMessage();
        }
    }

    private boolean hasConnectivity() {
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
}
