package com.example.tgzoom.letswatch;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import com.example.tgzoom.letswatch.data.movie.source.remote.MoviesRemoteDataSource;
import com.example.tgzoom.letswatch.util.schedulers.ImmediateScheduler;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.observers.TestSubscriber;

/**
 * Created by tgzoom on 1/2/17.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MoviesRemoteDataSourceTest {

    private MoviesRemoteDataSource moviesRemoteDataSource;

    @Before
    public void setup(){
        ImmediateScheduler immediateScheduler = new ImmediateScheduler();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addNetworkInterceptor(new StethoInterceptor()).build();
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .baseUrl("http://api.themoviedb.org")
                .client(okHttpClient)
                .build();
        moviesRemoteDataSource = new MoviesRemoteDataSource(retrofit, immediateScheduler);
    }

    @Test
    public void getMovies(){
        TestSubscriber testSubscriber = new TestSubscriber<>();
        moviesRemoteDataSource.getMovies("popularity.desc",1).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
    }
}
