package com.example.tgzoom.letswatch.data.source;

import android.content.Context;

import com.example.tgzoom.letswatch.data.source.local.MoviesLocalDataSource;
import com.example.tgzoom.letswatch.data.source.remote.MoviesRemoteDataSource;
import com.example.tgzoom.letswatch.favourites.FavouriteObservable;
import com.example.tgzoom.letswatch.favourites.FavouriteObservableImp;
import com.example.tgzoom.letswatch.util.schedulers.BaseScheduler;

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
    MoviesDataSource provideMoviesLocalDataSource(Context context, BaseScheduler scheduler){
        return new MoviesLocalDataSource(context,scheduler);
    }

    @Singleton
    @Provides
    @Remote
    MoviesDataSource provideMoviesRemoteDataSource(Retrofit retrofit, BaseScheduler scheduler){
        return new MoviesRemoteDataSource(retrofit,scheduler);
    }

    @Singleton
    @Provides
    FavouriteObservable provideFavouriteMoviesObservable(BaseScheduler scheduler){
        return new FavouriteObservableImp(scheduler);
    }
}
