package com.example.tgzoom.letswatch;

import android.content.Context;
import android.net.ConnectivityManager;

import com.example.tgzoom.letswatch.data.source.MoviesRepository;
import com.example.tgzoom.letswatch.data.source.MoviesRepositoryModule;
import com.example.tgzoom.letswatch.service.ServiceModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;

/**
 * Created by tgzoom on 3/14/17.
 */

@Singleton
@Component(
        modules = {
                ServiceModule.class,
                MoviesRepositoryModule.class
        }
)
public interface AppComponent {
    Context getContext();
    MoviesRepository getMoviesRepository();
    ConnectivityManager getConnectivityManager();
}
