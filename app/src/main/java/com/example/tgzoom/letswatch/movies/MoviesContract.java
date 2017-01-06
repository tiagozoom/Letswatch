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
        void setLoadingIndicator(boolean active);
        void showMovies(List<Movie> movies);
        void showLoadingMoviesError();
        void updateMovies(int movieApiId,boolean isFavourite);
        boolean isActive();
        void showMarkedAsFavouriteMessage();
        void showUnmarkedAsFavouriteMessage();
        void showMovieDetails(Movie movie);
    }

    interface Presenter extends BasePresenter {
        void result(int requestCode,int resultCode);
        void loadMovies(@NonNull  boolean forceUpdate, int currentPage);
        void markAsFavourite(@NonNull Movie movie);
        void unmarkAsFavourite(@NonNull int movieApiId);
        void openDetails(Movie movie);
    }
}
