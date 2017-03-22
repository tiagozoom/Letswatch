package com.example.tgzoom.letswatch.search;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
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
        mFavouriteMoviesIds = mMoviesRepository.getFavouriteMoviesIds();
    }

    @Inject
    void setupListeners() {
        mSearchView.setPresenter(this);
    }

    @Override
    public void loadMovies(String searchString, int page) {
        mSubscriptions.clear();
        mSearchView.setLoadingIndicator(true);
        mSearchView.showLoadingBar();

        Subscription subscription = mMoviesRepository
                .searchMovies(searchString, page)
                .withLatestFrom(mFavouriteMoviesIds, mMoviesRepository.getFavouriteMoviesIdsMapper())
                .subscribe(
                        new Observer<List<Movie>>() {
                            @Override
                            public void onCompleted() {
                                mSearchView.setLoadingIndicator(false);
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

    @Override
    public void markAsFavourite(@NonNull Movie movie) {
        mMoviesRepository.markAsFavourite(movie);
        mSearchView.showMarkedAsFavouriteMessage();
    }

    @Override
    public void unmarkAsFavourite(@NonNull int movieApiId) {
        mMoviesRepository.unmarkAsFavourite(movieApiId);
        mSearchView.showUnmarkedAsFavouriteMessage();
    }

    public void processMovies(List<Movie> movies) {
        mSearchView.hideLoadingBar();
        if(movies.size() > 0){
            mSearchView.showMovies(movies);
        }else{
            mSearchView.setEndOfList();
        }
    }

    @Override
    public void start(String searchString) {
        loadMovies(searchString, 1);
    }

    @Override
    public void start(boolean showLoadingBar) {
    }

    @Override
    public void openDetails(Movie movie) {
        mSearchView.showMovieDetails(movie);
    }
}
