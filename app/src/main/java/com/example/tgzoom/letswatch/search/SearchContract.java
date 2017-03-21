package com.example.tgzoom.letswatch.search;

import com.example.tgzoom.letswatch.BasePresenter;

/**
 * Created by tgzoom on 3/20/17.
 */

public interface SearchContract extends BasePresenter {
    interface View{
        void setPresenter(SearchPresenter presenter);
        void setLoadingIndicator(boolean isLoading);
        void updateMovies(int movieApiId,boolean isFavourite);
        void showLoadingMoviesError();
        void hideRefresh();
    }

    interface Presenter{
        void loadMovies(String searchString);
        void start(String searchString);
    }
}
