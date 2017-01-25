package com.example.tgzoom.letswatch.network;

import android.content.Context;
import android.net.ConnectivityManager;

import com.example.tgzoom.letswatch.AppModule;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by tgzoom on 12/28/16.
 */

@Module(
        includes = {
                AppModule.class
        }
)
public class NetworkModule {

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addNetworkInterceptor(new StethoInterceptor()).build();
        return okHttpClient;
    }
}
