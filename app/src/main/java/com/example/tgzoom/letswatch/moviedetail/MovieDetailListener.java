package com.example.tgzoom.letswatch.moviedetail;

import com.example.tgzoom.letswatch.data.Movie;

/**
 * Created by tgzoom on 1/5/17.
 */

public interface MovieDetailListener {
    void onMarkAsFavorite(Movie movie);
    void onUnmarAsFavorite(int movieApiId);
}
