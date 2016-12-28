package com.example.tgzoom.letswatch.movies;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tgzoom on 12/27/16.
 */

@Module
public class MoviesPresenterModule {

    private final MoviesContract.View mView;

    public MoviesPresenterModule(MoviesContract.View view){
        mView = view;
    }

    @Provides
    MoviesContract.View provideMoviesContractView(){
        return mView;
    }
}
