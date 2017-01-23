package com.example.tgzoom.letswatch.moviedetail;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tgzoom on 1/5/17.
 */

@Module
public class MovieDetailPresenterModule {
    private final MovieDetailContract.View mView;

    public MovieDetailPresenterModule(MovieDetailContract.View view){
        mView = view;
    }

    @Provides
    MovieDetailContract.View provideMovieDetailContractView(){
        return mView;
    }
}
