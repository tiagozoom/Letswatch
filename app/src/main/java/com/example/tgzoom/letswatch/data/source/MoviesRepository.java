package com.example.tgzoom.letswatch.data.source;

import android.support.annotation.NonNull;

import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.Trailer;
import com.example.tgzoom.letswatch.favourites.FavouriteObservable;
import com.example.tgzoom.letswatch.favourites.FavouriteObservableImp;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by tgzoom on 12/27/16.
 */
@Singleton
public class MoviesRepository implements MoviesDataSource {

    private final MoviesDataSource mMoviesRemoteDataSource;
    private final MoviesDataSource mMoviesLocalDataSource;
    private final FavouriteObservable mFavouriteObservable;

    @Inject
    MoviesRepository(@Remote MoviesDataSource moviesRemoteDataSource, @Local MoviesDataSource moviesLocalDataSource, FavouriteObservable favouriteObservable) {
        mMoviesLocalDataSource = moviesLocalDataSource;
        mMoviesRemoteDataSource = moviesRemoteDataSource;
        mFavouriteObservable = favouriteObservable;
    }

    @Override
    public Observable<List<Movie>> getMovies(String sort, int pageIndex) {
        Observable<List<Movie>> movies = mMoviesRemoteDataSource.getMovies(sort, pageIndex);
        return movies;
    }

    @Override
    public Observable<List<Movie>> searchMovies(String searchString) {
        Observable<List<Movie>> movies = mMoviesRemoteDataSource.searchMovies(searchString);
        return movies;
    }

    public Observable<FavouriteObservableImp.FavouriteClickEvent> getFavouriteClickEvent(){
        return mFavouriteObservable.getFavoriteClickEventObservable();
    }

    @Override
    public Observable<Movie> getMovie(@NonNull int movieApiId) {
        return null;
    }

    @Override
    public long markAsFavourite(@NonNull Movie movie) {
        mFavouriteObservable.markAsFavourite(movie);
        return mMoviesLocalDataSource.markAsFavourite(movie);
    }

    @Override
    public void unmarkAsFavourite(@NonNull int movieApiId) {
        mFavouriteObservable.unmarkAsFavourite(movieApiId);
        mMoviesLocalDataSource.unmarkAsFavourite(movieApiId);
    }

    @Override
    public void refreshMovies() {

    }

    @Override
    public int deleteAllMovies() {
        return 0;
    }

    @Override
    public Observable<List<Integer>> getFavouriteMoviesIds() {
        return mMoviesLocalDataSource.getFavouriteMoviesIds();
    }

    @Override
    public Observable<List<Trailer>> getTrailers(int movieApiId) {
        return mMoviesRemoteDataSource.getTrailers(movieApiId);
    }

    @Override
    public Observable<List<Movie>> getFavouriteMovies() {
        return mMoviesLocalDataSource.getFavouriteMovies();
    }

    public Func2<List<Movie>, List<Integer>, List<Movie>> getFavouriteMoviesIdsMapper() {
        return new Func2<List<Movie>, List<Integer>, List<Movie>>() {
            @Override
            public List<Movie> call(List<Movie> movies, List<Integer> integers) {
                if(integers.size() == 0){
                    return movies;
                }

                for (Movie movie : movies) {
                    int position = integers.indexOf(movie.getApi_movie_id());

                    if (position >= 0) {
                        movie.setFavourite(true);
                        integers.remove(position);
                    }
                }
                return movies;
            }
        };
    }
}
