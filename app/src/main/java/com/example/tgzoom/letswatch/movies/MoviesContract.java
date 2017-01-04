package com.example.tgzoom.letswatch.movies;

import android.support.annotation.NonNull;

import com.example.tgzoom.letswatch.BasePresenter;
import com.example.tgzoom.letswatch.BaseState;
import com.example.tgzoom.letswatch.BaseView;
import com.example.tgzoom.letswatch.data.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgzoom on 12/27/16.
 */

public interface MoviesContract {

    interface View extends BaseView<Presenter>{
        void setLoadingIndicator(boolean active);
        void showMovies(List<Movie> movies);
        void showLoadingMoviesError();
        boolean isActive();
        void showMarkedAsFavouriteMessage();
        void showUnmarkedAsFavouriteMessage();
    }

    interface State extends BaseState{
        ArrayList<Movie> getMovies();
        int getCurrentPage();
    }

    interface Presenter extends BasePresenter<State> {
        void result(int requestCode,int resultCode);
        void loadMovies(@NonNull  boolean forceUpdate, int currentPage);
        void markAsFavourite(@NonNull Movie movie);
        void unmarkAsFavourite(@NonNull int movieApiId);
    }
}
