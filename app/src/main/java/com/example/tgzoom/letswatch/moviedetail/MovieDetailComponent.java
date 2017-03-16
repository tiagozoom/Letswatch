package com.example.tgzoom.letswatch.moviedetail;

import com.example.tgzoom.letswatch.AppComponent;
import com.example.tgzoom.letswatch.scope.FragmentScoped;

import dagger.Component;

/**
 * Created by tgzoom on 1/5/17.
 */

@FragmentScoped
@Component(
        dependencies = {
                AppComponent.class,
                MovieDetailPresenterModule.class
        }
)
public interface MovieDetailComponent {
    void inject(MovieDetailActivity activity);
}
