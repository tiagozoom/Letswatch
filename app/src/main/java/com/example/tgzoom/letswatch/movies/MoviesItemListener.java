package com.example.tgzoom.letswatch.movies;

import com.example.tgzoom.letswatch.data.Movie;

/**
 * Created by tgzoom on 1/5/17.
 */

public interface MoviesItemListener {
    void onClick(Movie movie);
    void onMarkAsFavorite(Movie movie);
    void onUnmarAsFavorite(int movieApiId);
}
