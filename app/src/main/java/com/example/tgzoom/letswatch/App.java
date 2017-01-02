package com.example.tgzoom.letswatch;

import android.app.Application;
import android.util.Log;

import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.source.DaggerMoviesRepositoryComponent;
import com.example.tgzoom.letswatch.data.source.MoviesRepository;
import com.example.tgzoom.letswatch.data.source.MoviesRepositoryComponent;
import com.example.tgzoom.letswatch.data.source.MoviesRepositoryModule;
import com.example.tgzoom.letswatch.network.ServiceModule;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by tgzoom on 12/27/16.
 */

public class App extends Application {

    private MoviesRepositoryComponent mMoviesRepositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mMoviesRepositoryComponent = DaggerMoviesRepositoryComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .serviceModule(new ServiceModule("http://api.themoviedb.org"))
                .moviesRepositoryModule(new MoviesRepositoryModule())
                .build();
    }

    public MoviesRepositoryComponent getMoviesRepositoryComponent(){
        return mMoviesRepositoryComponent;
    }
}
