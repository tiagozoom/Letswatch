package com.example.tgzoom.letswatch.network;

import com.example.tgzoom.letswatch.AppModule;
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
        OkHttpClient okHttpClient = new OkHttpClient();
        return okHttpClient;
    }
}
