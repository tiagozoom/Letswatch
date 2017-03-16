package com.example.tgzoom.letswatch.favourites;

import com.example.tgzoom.letswatch.AppComponent;
import com.example.tgzoom.letswatch.scope.FragmentScoped;

import dagger.Component;

/**
 * Created by tgzoom on 12/28/16.
 */

@FragmentScoped
@Component(
        dependencies= {
                FavouritesPresenterModule.class,
                AppComponent.class,
        }
)
public interface FavouritesComponent {
        void inject(FavouritesFragment fragment);
}
