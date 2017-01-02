package com.example.tgzoom.letswatch.favourites;

import com.example.tgzoom.letswatch.data.source.MoviesRepositoryComponent;
import com.example.tgzoom.letswatch.main.MainActivity;
import com.example.tgzoom.letswatch.util.FragmentScoped;

import dagger.Component;

/**
 * Created by tgzoom on 12/28/16.
 */

public interface FavouritesComponent {
    void inject(MainActivity activity);
}
