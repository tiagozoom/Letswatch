package com.example.tgzoom.letswatch.listener;

import android.view.View;

import com.example.tgzoom.letswatch.data.Movie;

/**
 * Created by tgzoom on 1/5/17.
 */

public interface MoviesItemListener {
    void onClick(Movie movie);
    void onCardMenuClick(View view,Movie movie);
    void onMarkAsFavorite(Movie movie);
    void onUnmarAsFavorite(int movieApiId);
}
