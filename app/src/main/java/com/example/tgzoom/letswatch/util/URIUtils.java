package com.example.tgzoom.letswatch.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.example.tgzoom.letswatch.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tgzoom on 1/2/17.
 */

public class URIUtils {
    private static final String YOUTUBE_VIDEO_PARAMETER = "v";

    public static String buildPosterPath(String poster_path) {
        Uri.Builder builder = new Uri.Builder();
        try {
            poster_path = poster_path.substring(1);
            Uri poster_uri = builder
                    .scheme("https")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath("w300")
                    .appendPath(poster_path).build();
            return poster_uri.toString();
        } catch (Exception e) {
            Log.i("Letswatch", "An error occurred " + e);
        }

        return null;
    }

    public static String buildPosterPathW45(String poster_path) {
        Uri.Builder builder = new Uri.Builder();
        try {
            poster_path = poster_path.substring(1);
            Uri poster_uri = builder
                    .scheme("https")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath("w45")
                    .appendPath(poster_path).build();
            return poster_uri.toString();
        } catch (Exception e) {
            Log.i("Letswatch", "An error occurred " + e);
        }

        return null;
    }

    public static String buildBackDropPath(String backdrop_path) {
        Uri.Builder builder = new Uri.Builder();
        try {
            backdrop_path = backdrop_path.substring(1);
            Uri poster_uri = builder
                    .scheme("https")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath("w1000")
                    .appendPath(backdrop_path).build();
            return poster_uri.toString();
        } catch (Exception e) {
            Log.i("Letswatch", "An error occurred " + e);
        }
        return null;
    }

    public static Uri builBaseUrl() {
        return new Uri.Builder()
                .scheme("http")
                .authority("api.themoviedb.org")
                .build();
    }

    public static Uri buildYoutubeBaseUrl(String videoPath) {
        return new Uri.Builder().scheme("http").authority("youtube.com").appendPath("watch").appendQueryParameter(YOUTUBE_VIDEO_PARAMETER, videoPath).build();
    }

    public static Retrofit buildRetrofitObject() {
        return new Retrofit.Builder()
                .baseUrl(builBaseUrl().toString())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static String buildMovieDbURI(int movieId){
        String movieIdString = String.valueOf(movieId);
        return new Uri.Builder().scheme("https").authority("www.themoviedb.org").appendPath("movie").appendPath(movieIdString).build().toString();
    }
}
