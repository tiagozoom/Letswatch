package com.example.tgzoom.letswatch.util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by tgzoom on 12/28/16.
 */

public class ActivityUtils {

    public static void addFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @NonNull String tag,@NonNull int fragment_content){
        fragmentManager
                .beginTransaction()
                .add(fragment_content,fragment,tag)
                .commit();
    }
}
