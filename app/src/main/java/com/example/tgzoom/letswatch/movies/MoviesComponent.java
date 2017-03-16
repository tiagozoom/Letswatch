package com.example.tgzoom.letswatch.movies;

import android.content.Context;

import com.example.tgzoom.letswatch.AppComponent;
import com.example.tgzoom.letswatch.scope.FragmentScoped;
import com.example.tgzoom.letswatch.service.ServiceModule;

import dagger.Component;

/**
 * Created by tgzoom on 1/5/17.
 */

@FragmentScoped
@Component(
        dependencies = {
                AppComponent.class,
                MoviesPresenterModule.class,
        }
)
public interface MoviesComponent {
    void inject(MoviesFragment fragment);
}
