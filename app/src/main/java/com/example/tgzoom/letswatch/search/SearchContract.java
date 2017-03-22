package com.example.tgzoom.letswatch.search;

import android.support.annotation.NonNull;

import com.example.tgzoom.letswatch.BasePresenter;
import com.example.tgzoom.letswatch.data.Movie;

import java.util.List;

/**
 * Created by tgzoom on 3/20/17.
 */

public interface SearchContract extends BasePresenter {
    interface View{
        void setPresenter(SearchPresenter presenter);
        void setLoadingIndicator(boolean isLoading);
        void updateMovies(int movieApiId,boolean isFavourite);
        void showMarkedAsFavouriteMessage();
        void showUnmarkedAsFavouriteMessage();
        void showLoadingMoviesError();
        void hideRefresh();
        void showMovies(List<Movie> movies);
        void loadMovies(String searchString);
        void showMovieDetails(Movie movie);
        void showLoadingBar();
        void hideLoadingBar();
        void setEndOfList();
    }

    interface Presenter extends BasePresenter{
        void loadMovies(String searchString,int page);
        void markAsFavourite(@NonNull Movie movie);
        void unmarkAsFavourite(@NonNull int movieApiId);
        void start(String searchString);
        void openDetails(Movie movie);
    }
}
