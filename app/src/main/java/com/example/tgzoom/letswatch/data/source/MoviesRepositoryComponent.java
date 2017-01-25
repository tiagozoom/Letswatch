package com.example.tgzoom.letswatch.data.source;

import android.content.SharedPreferences;

import com.example.tgzoom.letswatch.service.ServiceModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by tgzoom on 12/27/16.
 */

@Singleton
@Component(
        modules = {
                ServiceModule.class,
                MoviesRepositoryModule.class
        }
)
public interface MoviesRepositoryComponent {
    MoviesRepository getMoviesRepository();
}