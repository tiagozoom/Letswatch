package com.example.tgzoom.letswatch.search;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.source.MoviesRepository;
import com.example.tgzoom.letswatch.favourites.FavouriteObservableImp;
import com.example.tgzoom.letswatch.movies.MoviesContract;
import com.example.tgzoom.letswatch.util.PreferencesUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tgzoom on 3/20/17.
 */

public class SearchPresenter implements SearchContract.Presenter {

    private final MoviesRepository mMoviesRepository;
    private final SearchContract.View mSearchView;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private Observable<List<Integer>> mFavouriteMoviesIds;

    @Inject
    public SearchPresenter(MoviesRepository moviesRepository, SearchContract.View searchView) {
        mMoviesRepository = moviesRepository;
        mSearchView = searchView;
    }

    @Inject
    void setupListeners() {
        mSearchView.setPresenter(this);
    }

    @Override
    public void loadMovies(String searchString) {
        mSubscriptions.clear();
        mSearchView.setLoadingIndicator(true);

        Subscription subscription = mMoviesRepository
                .searchMovies(searchString)
                .withLatestFrom(mFavouriteMoviesIds, mMoviesRepository.getFavouriteMoviesIdsMapper())
                .retry()
                .subscribe(
                        new Observer<List<Movie>>() {
                            @Override
                            public void onCompleted() {
                                mSearchView.setLoadingIndicator(false);
                                mSearchView.hideRefresh();
                            }

                            @Override
                            public void onError(Throwable e) {
                                mSearchView.setLoadingIndicator(false);
                                mSearchView.showLoadingMoviesError();
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
                        mSearchView.updateMovies(favouriteClickEvent.movieApiId, favouriteClickEvent.isFavorite);
                    }
                })
        );

    }

    public void processMovies(List<Movie> movies){
        Log.i("teste",movies.toString());
    }

    @Override
    public void start(String searchString) {
        loadMovies(searchString);
    }
}
