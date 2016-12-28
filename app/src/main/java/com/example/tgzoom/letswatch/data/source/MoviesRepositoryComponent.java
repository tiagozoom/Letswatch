package com.example.tgzoom.letswatch.data.source;

import android.app.Service;

import com.example.tgzoom.letswatch.AppModule;
import com.example.tgzoom.letswatch.network.NetworkModule;
import com.example.tgzoom.letswatch.network.ServiceModule;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by tgzoom on 12/27/16.
 */

@Singleton
@Component(
        modules = {
                ServiceModule.class, MoviesRepositoryModule.class
        }
)
public interface MoviesRepositoryComponent {
    MoviesRepository getMoviesRepository();
}
