package com.example.tgzoom.letswatch.moviedetail;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.tgzoom.letswatch.BasePresenter;
import com.example.tgzoom.letswatch.BaseView;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.Trailer;

import java.util.List;

/**
 * Created by tgzoom on 1/5/17.
 */

public interface MovieDetailContract {
    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);
        void showMarkedAsFavouriteMessage();
        void showUnmarkedAsFavouriteMessage();
        void showTrailers(List<Trailer> trailers);
        void openTrailer(Uri trailerUri);
        void shareMovie(Intent shareMovieIntent);
    }

    interface Presenter extends BasePresenter {
        void markAsFavourite(@NonNull Movie movie);
        void unmarkAsFavourite(@NonNull int movieApiId);
        void openTrailer(String trailerUri);
        void loadTrailers(int movieApiId);
        void processTrailers(List<Trailer> trailers);
        void shareMovie(int movie_Api_Id);
    }
}
