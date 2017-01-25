package com.example.tgzoom.letswatch.movies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.source.MoviesRepository;
import com.example.tgzoom.letswatch.favourites.FavouriteObservableImp;
import com.example.tgzoom.letswatch.util.PreferencesUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tgzoom on 12/27/16.
 */

public class MoviesPresenter implements MoviesContract.Presenter {

    private final MoviesRepository mMoviesRepository;
    private final MoviesContract.View mMoviesView;
    private final Context mContext;
    private CompositeSubscription mSubscriptions  = new CompositeSubscription();
    private ConnectivityManager mConnectivityManager;
    private Observable<List<Integer>> mFavouriteMoviesIds;

    @Inject
    MoviesPresenter(MoviesRepository moviesRepository, MoviesContract.View moviesView, Context context, ConnectivityManager connectivityManager) {
        mMoviesRepository = moviesRepository;
        mMoviesView = moviesView;
        mFavouriteMoviesIds = mMoviesRepository.getFavouriteMoviesIds();
        mContext = context;
        mConnectivityManager = connectivityManager;
    }

    @Inject
    void setupListeners() {
        mMoviesView.setPresenter(this);
    }

    @Override
    public void start() {
        loadMovies(false, 1);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    public void loadMovies(@NonNull boolean forceUpdate, int currentPage) {
        mSubscriptions.clear();
        if(hasConnectivity()) {
            mMoviesView.setLoadingIndicator(true);
            Subscription subscription = mMoviesRepository
                    .getMovies(PreferencesUtils.getPreferredSortOrder(mContext), currentPage)
                    .withLatestFrom(mFavouriteMoviesIds, mMoviesRepository.getFavouriteMoviesIdsMapper())
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
            mSubscriptions.add(mMoviesRepository.getFavouriteClickEvent().subscribe(
                    new Action1<FavouriteObservableImp.FavouriteClickEvent>() {
                        @Override
                        public void call(FavouriteObservableImp.FavouriteClickEvent favouriteClickEvent) {
                            mMoviesView.updateMovies(favouriteClickEvent.movieApiId, favouriteClickEvent.isFavorite);
                        }
                    })
            );
        }
    }

    private void processMovies(@NonNull List<Movie> movies) {
        mMoviesView.showMovies(movies);
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

    @Override
    public void testConnectivity() {
        if(hasConnectivity()){
            mMoviesView.hideMessage();
        }else{
            mMoviesView.showNoConnectivityMessage();
        }
    }

    private boolean hasConnectivity(){
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
}
