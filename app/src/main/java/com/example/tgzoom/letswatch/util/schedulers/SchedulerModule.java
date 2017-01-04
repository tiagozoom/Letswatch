package com.example.tgzoom.letswatch.util.schedulers;

import android.content.Context;

import com.example.tgzoom.letswatch.data.source.Local;
import com.example.tgzoom.letswatch.data.source.MoviesDataSource;
import com.example.tgzoom.letswatch.data.source.local.MoviesLocalDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tgzoom on 1/4/17.
 */

@Module
public class SchedulerModule {

    @Provides
    public BaseScheduler provideScheduler(){
        return new Scheduler();
    }
}
