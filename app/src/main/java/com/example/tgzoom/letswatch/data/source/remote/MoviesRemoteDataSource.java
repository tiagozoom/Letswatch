package com.example.tgzoom.letswatch.data.source.remote;

import android.support.annotation.NonNull;

import com.example.tgzoom.letswatch.BuildConfig;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.Trailer;
import com.example.tgzoom.letswatch.data.source.MoviesDataSource;
import com.example.tgzoom.letswatch.data.source.remote.services.MoviesServiceInterface;
import com.example.tgzoom.letswatch.util.schedulers.BaseScheduler;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by tgzoom on 12/27/16.
 */

public class MoviesRemoteDataSource implements MoviesDataSource{

    private Retrofit mRetrofit;
    private BaseScheduler mScheduler;

    public MoviesRemoteDataSource(Retrofit retrofit, BaseScheduler scheduler){
        mRetrofit = retrofit;
        mScheduler = scheduler;
    }

    @Override
    public Observable<List<Movie>> getMovies(String sort, int pageIndex) {
        MoviesServiceInterface moviesServiceInterface = mRetrofit.create(MoviesServiceInterface.class);
        Observable<List<Movie>> movies = moviesServiceInterface.getMovies(sort,pageIndex, BuildConfig.MOVIE_DB_API_KEY)
                .map(new Func1<Movie.Results, List<Movie>>() {
                    @Override
                    public List<Movie> call(Movie.Results results) {
                        return results.movies;
                    }
                })
                .subscribeOn(mScheduler.io())
                .observeOn(mScheduler.ui());
        return movies;
    }

    @Override
    public Observable<List<Movie>> getFavouriteMovies() {
        return null;
    }

    @Override
    public Observable<Movie> getMovie(@NonNull int movieApiId) {
        return null;
    }

    @Override
    public Observable<List<Integer>> getFavouriteMoviesIds() {return null;}

    @Override
    public Observable<List<Trailer>> getTrailers(int movieApiId) {
        MoviesServiceInterface moviesServiceInterface = mRetrofit.create(MoviesServiceInterface.class);
        Observable<List<Trailer>> trailers = moviesServiceInterface.getTrailers(movieApiId,BuildConfig.MOVIE_DB_API_KEY)
                .map(new Func1<Trailer.Results, List<Trailer>>() {
                    @Override
                    public List<Trailer> call(Trailer.Results results) {
                        return results.trailers;
                    }
                })
                .subscribeOn(mScheduler.io())
                .observeOn(mScheduler.ui());
        return trailers;
    }

    @Override
    public long markAsFavourite(@NonNull Movie movie) { return 0;}

    @Override
    public void unmarkAsFavourite(@NonNull int movieApiId) {}

    @Override
    public void refreshMovies() {

    }

    @Override
    public int deleteAllMovies() {
        return 0;
    }
}
