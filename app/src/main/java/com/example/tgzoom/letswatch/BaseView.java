package com.example.tgzoom.letswatch;

import android.os.Bundle;

import com.example.tgzoom.letswatch.movies.MoviesContract;

/**
 * Created by tgzoom on 12/27/16.
 */

public interface BaseView<T> {
    void setPresenter(T presenter);
}
