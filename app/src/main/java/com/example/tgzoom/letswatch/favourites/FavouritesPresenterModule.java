package com.example.tgzoom.letswatch.favourites;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tgzoom on 12/27/16.
 */

@Module
public class FavouritesPresenterModule {

    private final FavouritesContract.View mView;

    public FavouritesPresenterModule(FavouritesContract.View view){
        mView = view;
    }

    @Provides
    public FavouritesContract.View provideFavouritesContractView(){
        return mView;
    }
}
