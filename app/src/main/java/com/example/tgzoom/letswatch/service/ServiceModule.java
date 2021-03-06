package com.example.tgzoom.letswatch.service;

import android.content.Context;
import android.net.ConnectivityManager;
import com.example.tgzoom.letswatch.network.NetworkModule;
import com.example.tgzoom.letswatch.util.schedulers.BaseScheduler;
import com.example.tgzoom.letswatch.util.schedulers.Scheduler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tgzoom on 12/28/16.
 */
@Module(
        includes = {
                NetworkModule.class,
        }
)
public class ServiceModule {

    String mBaseUrl;

    public ServiceModule(String baseUrl){
        mBaseUrl = baseUrl;
    }

    @Singleton
    @Provides
    Gson provideGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
    }

    @Singleton
    @Provides
    BaseScheduler provideScheduler(){
        return Scheduler.getInstance();
    }

    @Singleton
    @Provides
    ConnectivityManager provideConnectivityManager(Context context){
        return (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
    }
}
