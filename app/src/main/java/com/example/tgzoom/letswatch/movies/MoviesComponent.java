package com.example.tgzoom.letswatch.movies;

import com.example.tgzoom.letswatch.data.source.MoviesRepositoryComponent;
import com.example.tgzoom.letswatch.main.MainActivity;
import com.example.tgzoom.letswatch.util.FragmentScoped;

import dagger.Component;

/**
 * Created by tgzoom on 12/28/16.
 */

@FragmentScoped
@Component(
        dependencies = {
                MoviesPresenterModule.class,
                MoviesRepositoryComponent.class
        }
)
public interface MoviesComponent {
    void inject(MoviesFragment fragment);
}
