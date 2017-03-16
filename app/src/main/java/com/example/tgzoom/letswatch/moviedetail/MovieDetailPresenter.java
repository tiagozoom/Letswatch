package com.example.tgzoom.letswatch.moviedetail;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.Trailer;
import com.example.tgzoom.letswatch.data.source.MoviesRepository;
import com.example.tgzoom.letswatch.favourites.FavouriteObservableImp;
import com.example.tgzoom.letswatch.util.URIUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tgzoom on 1/5/17.
 */

public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    private final MoviesRepository mMoviesRepository;

    private final MovieDetailContract.View mMovieDetailView;

    private CompositeSubscription mSubscriptions;

    @Inject
    MovieDetailPresenter(MoviesRepository moviesRepository, MovieDetailContract.View movieDetailView) {
        mMoviesRepository = moviesRepository;
        mMovieDetailView = movieDetailView;
        mSubscriptions = new CompositeSubscription();
    }

    @Inject
    void setupListeners() {
        mMovieDetailView.setPresenter(this);
    }

    @Override
    public void openTrailer(String trailerkey) {
        Uri trailerUri = URIUtils.buildYoutubeBaseUrl(trailerkey);
        mMovieDetailView.openTrailer(trailerUri);
    }

    @Override
    public void loadTrailers(int movieApiId) {
        mSubscriptions.clear();
        mMovieDetailView.setLoadingIndicator(true);
        Subscription subscription = mMoviesRepository
                .getTrailers(movieApiId)
                .subscribe(
                        new Observer<List<Trailer>>() {
                            @Override
                            public void onCompleted() {
                                mMovieDetailView.setLoadingIndicator(false);
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(List<Trailer> trailers) {
                                processTrailers(trailers);
                            }
                        }
                );

        mSubscriptions.add(subscription);
    }

    @Override
    public void processTrailers(List<Trailer> trailers) {
        mMovieDetailView.showTrailers(trailers);
    }

    @Override
    public void shareMovie(int movie_Api_Id) {
        String url = URIUtils.buildMovieDbURI(movie_Api_Id);
        Intent shareMovieIntent = new Intent(Intent.ACTION_SEND);
        shareMovieIntent.setType("text/plain");
        shareMovieIntent.putExtra(Intent.EXTRA_TEXT,url);
        mMovieDetailView.shareMovie(shareMovieIntent);
    }

    @Override
    public void markAsFavourite(@NonNull Movie movie) {
        mMoviesRepository.markAsFavourite(movie);
        mMovieDetailView.showMarkedAsFavouriteMessage();
    }

    @Override
    public void unmarkAsFavourite(@NonNull int movieApiId) {
        mMoviesRepository.unmarkAsFavourite(movieApiId);
        mMovieDetailView.showUnmarkedAsFavouriteMessage();
    }

    @Override
    public void start(boolean showLoadingBar) {}
}
