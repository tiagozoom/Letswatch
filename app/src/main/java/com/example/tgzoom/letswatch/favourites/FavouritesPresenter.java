package com.example.tgzoom.letswatch.favourites;

import android.support.annotation.NonNull;

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

    private CompositeSubscription mSubscriptions;

    @Inject
    FavouritesPresenter(MoviesRepository moviesRepository, FavouritesContract.View favouritesView){
        mMoviesRepository = moviesRepository;
        mFavouritesView = favouritesView;
        mSubscriptions = new CompositeSubscription();

    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadMovies(@NonNull boolean forceUpdate) {
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
                                processMovies(movies);
                            }
                        }
                );

        mSubscriptions.add(subscription);
        mSubscriptions.add(mMoviesRepository.getFavouriteClickEvent().subscribe(
                new Action1<FavouriteObservableImp.FavouriteClickEvent>() {
                    @Override
                    public void call(FavouriteObservableImp.FavouriteClickEvent favouriteClickEvent) {
                        mFavouritesView.updateMovies(favouriteClickEvent.movieApiId, favouriteClickEvent.isFavorite);
                    }
                })
        );
    }

    private void processMovies(@NonNull List<Movie> movies) {
        mFavouritesView.showMovies(movies);
    }

    @Override
    public void markAsFavourite(@NonNull Movie movie) {

    }

    @Override
    public void unmarkAsFavourite(@NonNull int movieApiId) {

    }

    @Override
    public void openDetails(Movie movie) {
        mFavouritesView.showMovieDetails(movie);
    }

    @Override
    public void start() {

    }

}
