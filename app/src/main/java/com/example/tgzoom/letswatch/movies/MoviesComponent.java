package com.example.tgzoom.letswatch.movies;

import com.example.tgzoom.letswatch.data.source.MoviesRepositoryComponent;
import com.example.tgzoom.letswatch.main.MainActivity;
import com.example.tgzoom.letswatch.moviedetail.MovieDetailActivity;
import com.example.tgzoom.letswatch.moviedetail.MovieDetailPresenterModule;
import com.example.tgzoom.letswatch.util.FragmentScoped;

import dagger.Component;

/**
 * Created by tgzoom on 1/5/17.
 */

@FragmentScoped
@Component(
        dependencies = {
                MoviesRepositoryComponent.class,
                MoviesPresenterModule.class
        }
)
public interface MoviesComponent {
    void inject(MoviesFragment fragment);
}
