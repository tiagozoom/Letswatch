package com.example.tgzoom.letswatch.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.Trailer;
import com.example.tgzoom.letswatch.data.source.MoviesDataSource;
import com.example.tgzoom.letswatch.util.schedulers.BaseScheduler;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by tgzoom on 12/27/16.
 */

public class MoviesLocalDataSource implements MoviesDataSource {

    private BriteDatabase mMoviesDbHelper;
    private BaseScheduler mScheduler;

    public MoviesLocalDataSource(@NonNull Context context, BaseScheduler scheduler){
        MoviesDbHelper moviesDbHelper = new MoviesDbHelper(context);
        SqlBrite sqlBrite = SqlBrite.create();
        mMoviesDbHelper = sqlBrite.wrapDatabaseHelper(moviesDbHelper, scheduler.io());
        mScheduler = scheduler;
    }

    @NonNull
    private Func1<Cursor,Integer> mMovieIdMapperFunction = new Func1<Cursor, Integer>() {
        @Override
        public Integer call(Cursor cursor) {
            int movieApiId = cursor.getInt(cursor.getColumnIndexOrThrow(MoviesPersistenceContract.MovieEntry.COLUMN_API_MOVIE_ID));
            return movieApiId;
        }
    };

    @NonNull
    private Func1<Cursor,Movie> mMovieMapperFunction = new Func1<Cursor, Movie>() {
        @Override
        public Movie call(Cursor cursor) {
            Movie movie = new Movie();
            long movieId = cursor.getLong(cursor.getColumnIndexOrThrow(MoviesPersistenceContract.MovieEntry._ID));
            int movieApiId = cursor.getInt(cursor.getColumnIndexOrThrow(MoviesPersistenceContract.MovieEntry.COLUMN_API_MOVIE_ID));
            String backdropPath = cursor.getString(cursor.getColumnIndexOrThrow(MoviesPersistenceContract.MovieEntry.COLUMN_BACKDROP_PATH));
            String originalTitle = cursor.getString(cursor.getColumnIndexOrThrow(MoviesPersistenceContract.MovieEntry.COLUMN_ORIGINAL_TITLE));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(MoviesPersistenceContract.MovieEntry.COLUMN_OVERVIEW));
            Double popularity = cursor.getDouble(cursor.getColumnIndexOrThrow(MoviesPersistenceContract.MovieEntry.COLUMN_POPULARITY));
            String posterPath = cursor.getString(cursor.getColumnIndexOrThrow(MoviesPersistenceContract.MovieEntry.COLUMN_POSTER_PATH));
            String releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(MoviesPersistenceContract.MovieEntry.COLUMN_RELEASE_DATE));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MoviesPersistenceContract.MovieEntry.COLUMN_TITLE));
            Double voteAverage = cursor.getDouble(cursor.getColumnIndexOrThrow(MoviesPersistenceContract.MovieEntry.COLUMN_VOTE_AVERAGE));
            int voteCount = cursor.getInt(cursor.getColumnIndexOrThrow(MoviesPersistenceContract.MovieEntry.COLUMN_VOTE_COUNT));
            boolean isFavourite = cursor.getInt(cursor.getColumnIndexOrThrow(MoviesPersistenceContract.MovieEntry.COLUMN_FAVOURITE)) == 1;

            movie.setApi_movie_id(movieApiId);
            movie.setBackdrop_path(backdropPath);
            movie.setOriginal_title(originalTitle);
            movie.setOverview(overview);
            movie.setPopularity(popularity);
            movie.setFavourite(isFavourite);
            movie.setRelease_date(releaseDate);
            movie.setVote_average(voteAverage);
            movie.setVote_count(voteCount);
            movie.setPoster_path(posterPath);
            movie.setTitle(title);
            movie.setId(movieId);

            return movie;
        }
    };

    @Override
    public Observable<List<Movie>> getMovies(String sort, int pageIndex) {
        return null;
    }

    @Override
    public Observable<List<Movie>> searchMovies(String searchString) {
        return null;
    }

    @Override
    public Observable<List<Movie>> getFavouriteMovies() {
        String[] projection = new String[]{
                MoviesPersistenceContract.MovieEntry._ID,
                MoviesPersistenceContract.MovieEntry.COLUMN_API_MOVIE_ID,
                MoviesPersistenceContract.MovieEntry.COLUMN_BACKDROP_PATH,
                MoviesPersistenceContract.MovieEntry.COLUMN_FAVOURITE,
                MoviesPersistenceContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
                MoviesPersistenceContract.MovieEntry.COLUMN_OVERVIEW,
                MoviesPersistenceContract.MovieEntry.COLUMN_POPULARITY,
                MoviesPersistenceContract.MovieEntry.COLUMN_POSTER_PATH,
                MoviesPersistenceContract.MovieEntry.COLUMN_RELEASE_DATE,
                MoviesPersistenceContract.MovieEntry.COLUMN_TITLE,
                MoviesPersistenceContract.MovieEntry.COLUMN_VOTE_AVERAGE,
                MoviesPersistenceContract.MovieEntry.COLUMN_VOTE_COUNT,
        };

        String sql = String.format("SELECT %s FROM %s ORDER BY _id DESC", TextUtils.join(",", projection), MoviesPersistenceContract.MovieEntry.TABLE_NAME);

        return mMoviesDbHelper.createQuery(MoviesPersistenceContract.MovieEntry.TABLE_NAME,sql).mapToList(mMovieMapperFunction).observeOn(mScheduler.ui());
    }

    @Override
    public Observable<Movie> getMovie(@NonNull int movieApiId) {
        String[] projection = new String[]{
                MoviesPersistenceContract.MovieEntry._ID,
                MoviesPersistenceContract.MovieEntry.COLUMN_API_MOVIE_ID,
                MoviesPersistenceContract.MovieEntry.COLUMN_BACKDROP_PATH,
                MoviesPersistenceContract.MovieEntry.COLUMN_FAVOURITE,
                MoviesPersistenceContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
                MoviesPersistenceContract.MovieEntry.COLUMN_OVERVIEW,
                MoviesPersistenceContract.MovieEntry.COLUMN_POPULARITY,
                MoviesPersistenceContract.MovieEntry.COLUMN_POSTER_PATH,
                MoviesPersistenceContract.MovieEntry.COLUMN_RELEASE_DATE,
                MoviesPersistenceContract.MovieEntry.COLUMN_TITLE,
                MoviesPersistenceContract.MovieEntry.COLUMN_VOTE_AVERAGE,
                MoviesPersistenceContract.MovieEntry.COLUMN_VOTE_COUNT,
        };

        String sql = String.format("SELECT %s FROM %s WHERE %s LIKE ?", TextUtils.join(MoviesDbHelper.COMMA_SEPARATOR, projection), MoviesPersistenceContract.MovieEntry.TABLE_NAME, MoviesPersistenceContract.MovieEntry.COLUMN_API_MOVIE_ID);

        return mMoviesDbHelper.createQuery(MoviesPersistenceContract.MovieEntry.TABLE_NAME,sql,String.valueOf(movieApiId)).mapToOneOrDefault(mMovieMapperFunction,null);
    }

    @Override
    public Observable<List<Integer>> getFavouriteMoviesIds() {
        String[] projection = new String[]{MoviesPersistenceContract.MovieEntry.COLUMN_API_MOVIE_ID};
        String sql = String.format("SELECT %s FROM %s", TextUtils.join(",", projection), MoviesPersistenceContract.MovieEntry.TABLE_NAME);
        return mMoviesDbHelper.createQuery(MoviesPersistenceContract.MovieEntry.TABLE_NAME,sql).mapToList(mMovieIdMapperFunction);
    }

    @Override
    public Observable<List<Trailer>> getTrailers(int movieApiId) {
        return null;
    }

    @Override
    public long markAsFavourite(@NonNull Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_API_MOVIE_ID, movie.getApi_movie_id());
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdrop_path());
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPoster_path());
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_FAVOURITE, true);
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginal_title());
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease_date());
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVote_count());
        values.put(MoviesPersistenceContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVote_average());
        return mMoviesDbHelper.insert(MoviesPersistenceContract.MovieEntry.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public void unmarkAsFavourite(@NonNull int movieApiId) {
        String whereClause = " movie_api_id = ?";
        mMoviesDbHelper.delete(MoviesPersistenceContract.MovieEntry.TABLE_NAME,whereClause,String.valueOf(movieApiId));
    }

    @Override
    public void refreshMovies() {

    }

    @Override
    public int deleteAllMovies() {
        return mMoviesDbHelper.delete(MoviesPersistenceContract.MovieEntry.TABLE_NAME,null,null);
    }
}
