package com.example.tgzoom.letswatch.moviedetail;

import com.example.tgzoom.letswatch.data.source.MoviesRepositoryComponent;
import com.example.tgzoom.letswatch.main.MainActivity;
import com.example.tgzoom.letswatch.movies.MoviesPresenterModule;
import com.example.tgzoom.letswatch.util.FragmentScoped;

import dagger.Component;

/**
 * Created by tgzoom on 1/5/17.
 */

@FragmentScoped
@Component(
        dependencies = {
                MovieDetailPresenterModule.class,
                MoviesRepositoryComponent.class
        }
)
public interface MovieDetailComponent {
    void inject(MovieDetailActivity activity);
}
