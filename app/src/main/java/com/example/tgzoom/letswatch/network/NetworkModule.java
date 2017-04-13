package com.example.tgzoom.letswatch.network;

import android.content.Context;
import android.net.ConnectivityManager;

import com.example.tgzoom.letswatch.AppModule;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;
import java.util.UUID;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by tgzoom on 12/28/16.
 */

@Module(
        includes = {
                AppModule.class
        }
)
public class NetworkModule {

    private static long MAX_CACHE_SIZE = 1024;

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(Cache cache,HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(httpLoggingInterceptor)
                .cache(cache)
                .build();
        return okHttpClient;
    }

    @Singleton
    @Provides
    Cache provideOkHttpCache(File file, long maxCacheSize){
        return new Cache(file,maxCacheSize);
    }

    @Singleton
    @Provides
    File provideFile(){
        return new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
    }

    @Singleton
    @Provides
    long provideMaxCacheSize(){
        return MAX_CACHE_SIZE;
    }

    @Singleton
    @Provides
    HttpLoggingInterceptor provideHttpLogginInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }
}
