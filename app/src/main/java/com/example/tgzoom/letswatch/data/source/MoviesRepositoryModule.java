package com.example.tgzoom.letswatch.data.source;

import android.content.Context;

import com.example.tgzoom.letswatch.AppModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tgzoom on 12/27/16.
 */

public class MoviesRepositoryModule {
    @Singleton
    @Provides
    @Local
    MoviesDataSource provideMoviesLocalDataSource(Context context){
        return null;
    }

    @Singleton
    @Provides
    @Remote
    MoviesDataSource provideMoviesRemoteDataSource(Context context){
        return null;
    }
}
