package com.example.tgzoom.letswatch.moviedetail;

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
        void showMovie(Movie movies);
    }

    interface Presenter extends BasePresenter {
        void openTrailer(Trailer trailer);
    }
}
