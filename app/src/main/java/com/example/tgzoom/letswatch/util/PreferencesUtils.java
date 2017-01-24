package com.example.tgzoom.letswatch.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tgzoom.letswatch.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tgzoom on 1/24/17.
 */

public class PreferencesUtils {
    public static String getPreferredSortOrder(SharedPreferences sharedPreferences, Context context){
        return sharedPreferences.getString(context.getString(R.string.pref_order_key),context.getString(R.string.pref_order_default_value));
    }

    public static int getSortPosition(SharedPreferences sharedPreferences, Context context){
        String actual_sort = sharedPreferences.getString(context.getString(R.string.pref_order_key),context.getString(R.string.pref_order_default_value));
        List<String> values = new ArrayList<String>(Arrays.asList(context.getResources().getStringArray(R.array.pref_order_entry_values)));
        return values.indexOf(actual_sort);
    }

    public static void setPreferredOrder(Context context,String sort) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_order_key),Context.MODE_APPEND);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_order_key),sort);
        editor.commit();
    }
}
