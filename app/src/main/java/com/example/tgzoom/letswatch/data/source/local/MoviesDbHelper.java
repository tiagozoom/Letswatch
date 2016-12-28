package com.example.tgzoom.letswatch.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tgzoom on 12/28/16.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME    = "movies.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = "TEXT";
    private static final String BOOLEAN_TYPE = "INTEGER";
    private static final String COMMA_SEPARATOR = ",";

    private static final String SQL_CREATE_MOVIE_STATEMENT = "CREATE TABLE "+MoviesPersistenceContract.MovieEntry.TABLE_NAME+" ( "
            +MoviesPersistenceContract.MovieEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +MoviesPersistenceContract.MovieEntry.COLUMN_API_MOVIE_ID +" INTEGER NOT NULL,"
            +MoviesPersistenceContract.MovieEntry.COLUMN_ORIGINAL_TITLE+" TEXT NOT NULL,"
            +MoviesPersistenceContract.MovieEntry.COLUMN_OVERVIEW+" TEXT NOT NULL,"
            +MoviesPersistenceContract.MovieEntry.COLUMN_POSTER_PATH+" TEXT,"
            +MoviesPersistenceContract.MovieEntry.COLUMN_BACKDROP_PATH+" TEXT,"
            +MoviesPersistenceContract.MovieEntry.COLUMN_RELEASE_DATE+" STRING NOT NULL,"
            +MoviesPersistenceContract.MovieEntry.COLUMN_VOTE_AVERAGE +" REAL NOT NULL,"
            +MoviesPersistenceContract.MovieEntry.COLUMN_VOTE_COUNT+" INTEGER NOT NULL,"
            +MoviesPersistenceContract.MovieEntry.COLUMN_TITLE+" TEXT NOT NULL,"
            +MoviesPersistenceContract.MovieEntry.COLUMN_POPULARITY+" REAL NOT NULL,"
            +MoviesPersistenceContract.MovieEntry.COLUMN_FAVORED+" INTEGER DEFAULT 1,"
            +" UNIQUE (" + MoviesPersistenceContract.MovieEntry.COLUMN_API_MOVIE_ID +") ON CONFLICT REPLACE);";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesPersistenceContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
