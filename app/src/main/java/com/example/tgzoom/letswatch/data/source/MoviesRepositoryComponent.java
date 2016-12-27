package com.example.tgzoom.letswatch.data.source;

import com.example.tgzoom.letswatch.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by tgzoom on 12/27/16.
 */

@Singleton
@Component(modules = {MoviesRepositoryModule.class, AppModule.class})
public interface MoviesRepositoryComponent {
    MoviesRepository getMoviesRepository();
}
