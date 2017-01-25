package com.example.tgzoom.letswatch.movies;

import com.example.tgzoom.letswatch.AppModule;
import com.example.tgzoom.letswatch.data.source.MoviesRepositoryComponent;
import com.example.tgzoom.letswatch.scope.FragmentScoped;

import dagger.Component;

/**
 * Created by tgzoom on 1/5/17.
 */

@FragmentScoped
@Component(
        dependencies = {
                MoviesRepositoryComponent.class,
                MoviesPresenterModule.class,
                AppModule.class
        }
)
public interface MoviesComponent {
    void inject(MoviesFragment fragment);
}
