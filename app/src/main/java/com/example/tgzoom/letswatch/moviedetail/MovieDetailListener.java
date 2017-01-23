package com.example.tgzoom.letswatch.moviedetail;

import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.Trailer;

/**
 * Created by tgzoom on 1/5/17.
 */

public interface MovieDetailListener {
    void onMarkAsFavorite(Movie movie);
    void onUnmarAsFavorite(int movieApiId);
    void onTrailerClick(String trailerkey);
}
