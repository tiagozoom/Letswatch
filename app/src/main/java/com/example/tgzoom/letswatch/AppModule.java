package com.example.tgzoom.letswatch;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tgzoom on 12/27/16.
 */

@Module
public class AppModule {
    private final Context mContext;

    public AppModule(Context context){
        mContext = context;
    }

    @Provides
    public Context getContext(){
        return mContext;
    }
}
