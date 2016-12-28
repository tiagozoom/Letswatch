package com.example.tgzoom.letswatch.data.source;

import android.content.Context;

import com.example.tgzoom.letswatch.App;
import com.example.tgzoom.letswatch.AppModule;
import com.example.tgzoom.letswatch.data.source.local.MoviesLocalDataSource;
import com.example.tgzoom.letswatch.data.source.remote.MoviesRemoteDataSource;
import com.example.tgzoom.letswatch.network.NetworkModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by tgzoom on 12/27/16.
 */

@Module
public class MoviesRepositoryModule {
    @Singleton
    @Provides
    @Local
    MoviesDataSource provideMoviesLocalDataSource(Context context){
        return new MoviesLocalDataSource(context);
    }

    @Singleton
    @Provides
    @Remote
    MoviesDataSource provideMoviesRemoteDataSource(Retrofit retrofit){
        return new MoviesRemoteDataSource(retrofit);
    }
}
