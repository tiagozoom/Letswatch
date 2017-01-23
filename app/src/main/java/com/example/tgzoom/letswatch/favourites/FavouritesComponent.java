package com.example.tgzoom.letswatch.favourites;

import com.example.tgzoom.letswatch.data.source.MoviesRepositoryComponent;
import com.example.tgzoom.letswatch.service.FragmentScoped;

import dagger.Component;

/**
 * Created by tgzoom on 12/28/16.
 */

@FragmentScoped
@Component(
        dependencies= {
                FavouritesPresenterModule.class,
                MoviesRepositoryComponent.class,
        }
)
public interface FavouritesComponent {
        void inject(FavouritesFragment fragment);
}
