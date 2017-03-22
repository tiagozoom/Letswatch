package com.example.tgzoom.letswatch.movies;

import android.support.annotation.NonNull;

import com.example.tgzoom.letswatch.BasePresenter;
import com.example.tgzoom.letswatch.BaseView;
import com.example.tgzoom.letswatch.data.Movie;

import java.util.List;

/**
 * Created by tgzoom on 12/27/16.
 */

public interface MoviesContract {

    interface View extends BaseView<Presenter>{
        void setLoadingIndicator(boolean isLoading);
        void showMovies(List<Movie> movies);
        void showLoadingMoviesError();
        void updateMovies(int movieApiId,boolean isFavourite);
        void showMarkedAsFavouriteMessage();
        void showUnmarkedAsFavouriteMessage();
        void showNoConnectivityMessage();
        void showMovieDetails(Movie movie);
        void hideMessage();
        void hideRefresh();
        void showLoadingBar();
        void hideLoadingBar();
        void setEndOfList();
    }

    interface Presenter extends BasePresenter {
        void loadMovies(int currentPage,boolean showLoadingBar);
        void markAsFavourite(@NonNull Movie movie);
        void unmarkAsFavourite(@NonNull int movieApiId);
        void openDetails(Movie movie);
        void testConnectivity();
    }
}
