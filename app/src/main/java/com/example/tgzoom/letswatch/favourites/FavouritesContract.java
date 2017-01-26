package com.example.tgzoom.letswatch.favourites;

import android.support.annotation.NonNull;

import com.example.tgzoom.letswatch.BasePresenter;
import com.example.tgzoom.letswatch.BaseView;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.movies.MoviesContract;

import java.util.List;

/**
 * Created by tgzoom on 12/27/16.
 */

public interface FavouritesContract {

    interface View extends BaseView<FavouritesContract.Presenter> {
        void setLoadingIndicator(boolean active);
        void showMovies(List<Movie> movies);
        void showLoadingMoviesError();
        void showMarkedAsFavouriteMessage();
        void showUnmarkedAsFavouriteMessage();
        void showMovieDetails(Movie movie);
        void updateMovies(int movieApiId,boolean isFavourite);
        void showNoConnectivityMessage();
        void showLoadingBar();
        void hideLoadingBar();
        void hideMessage();
        void hideRefresh();
    }

    interface Presenter extends BasePresenter {
        void loadMovies(@NonNull boolean forceUpdate);
        void markAsFavourite(@NonNull Movie movie);
        void unmarkAsFavourite(@NonNull int movieApiId);
        void openDetails(Movie movie);
        void testConnectivity();
    }
}
