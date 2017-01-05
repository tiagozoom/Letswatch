package com.example.tgzoom.letswatch.moviedetail;

import android.support.annotation.NonNull;

import com.example.tgzoom.letswatch.BasePresenter;
import com.example.tgzoom.letswatch.BaseView;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.Trailer;

/**
 * Created by tgzoom on 1/5/17.
 */

public interface MovieDetailContract {
    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);
        void showMarkedAsFavouriteMessage();
        void showUnmarkedAsFavouriteMessage();
        void showMovie(Movie movie);
    }

    interface Presenter extends BasePresenter {
        void openTrailer(Trailer trailer);
        void markAsFavourite(@NonNull Movie movie);
        void unmarkAsFavourite(@NonNull int movieApiId);
    }
}
