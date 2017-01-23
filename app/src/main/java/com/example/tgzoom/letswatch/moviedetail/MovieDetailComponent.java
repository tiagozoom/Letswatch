package com.example.tgzoom.letswatch.moviedetail;

import com.example.tgzoom.letswatch.data.source.MoviesRepositoryComponent;
import com.example.tgzoom.letswatch.util.FragmentScoped;

import dagger.Component;

/**
 * Created by tgzoom on 1/5/17.
 */

@FragmentScoped
@Component(
        dependencies = {
                MoviesRepositoryComponent.class,
                MovieDetailPresenterModule.class
        }
)
public interface MovieDetailComponent {
    void inject(MovieDetailActivity activity);
}
