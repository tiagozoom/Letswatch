package com.example.tgzoom.letswatch.util;

import android.content.Context;

import com.example.tgzoom.letswatch.R;

/**
 * Created by tgzoom on 12/14/16.
 */

public class StringUtils {

    private final static String ELLIPSIS = "...";
    private final static int YEAR_INITIAL_POSITION = 0;
    private final static int YEAR_FINAL_POSITION = 4;

    public static String formatMovieTitle(Context context, String movie_title){
        String formated_movie_title = movie_title;
        int number_of_characters = context.getResources().getInteger(R.integer.movie_title_max_characters);

        if(movie_title.length() > number_of_characters){
            formated_movie_title = movie_title.substring(0,number_of_characters).concat(ELLIPSIS);
        }

        return formated_movie_title;
    }

    public static String formatMovieYear(String year){
        String formated_year = null;
        if(year.length() >= YEAR_FINAL_POSITION){
            formated_year = year.substring(YEAR_INITIAL_POSITION,YEAR_FINAL_POSITION);
        }
        return formated_year;
    }
}
