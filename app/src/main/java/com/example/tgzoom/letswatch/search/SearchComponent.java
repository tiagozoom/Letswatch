package com.example.tgzoom.letswatch.search;

import com.example.tgzoom.letswatch.AppComponent;
import com.example.tgzoom.letswatch.AppModule;
import com.example.tgzoom.letswatch.data.source.MoviesRepository;
import com.example.tgzoom.letswatch.data.source.MoviesRepositoryModule;
import com.example.tgzoom.letswatch.scope.FragmentScoped;

import dagger.Component;

/**
 * Created by tgzoom on 3/20/17.
 */
@FragmentScoped
@Component(
        dependencies = {
                AppComponent.class,
                SearchPresenterModule.class
        }
)
public interface SearchComponent {
    void inject(SearchFragment searchFragment);
}
