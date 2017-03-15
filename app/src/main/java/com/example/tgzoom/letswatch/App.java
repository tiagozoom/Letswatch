package com.example.tgzoom.letswatch;

import android.app.Application;
import com.example.tgzoom.letswatch.data.source.MoviesRepositoryModule;
import com.example.tgzoom.letswatch.service.ServiceModule;

/**
 * Created by tgzoom on 12/27/16.
 */

public class App extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .serviceModule(new ServiceModule("http://api.themoviedb.org"))
                .moviesRepositoryModule(new MoviesRepositoryModule())
                .build();
    }

    public AppComponent getmAppComponent(){
        return mAppComponent;
    }
}
