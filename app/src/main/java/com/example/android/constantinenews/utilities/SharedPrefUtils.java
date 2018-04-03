package com.example.android.constantinenews.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.constantinenews.R;

/**
 * Created by merouane on 16/02/2018.
 */

public class SharedPrefUtils {

    public static long getLastUpdateTime(Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        long lastUpdateTime = prefs.getLong(context.getResources().getString(R.string.pref_update_time_key), 0);

        return lastUpdateTime;
    }

    public static void updateLastUpdateTime(Context context, long newTime){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(context.getResources().getString(R.string.pref_update_time_key), newTime);
        editor.commit();
    }
    /*
    public static boolean isItFirstTime(Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isIt = prefs.getBoolean(context.getResources().getString(R.string.pref_first_time_key), true);

        return isIt;
    }

    public static void writeToFirstTime(Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(context.getResources().getString(R.string.pref_first_time_key), false);
        editor.commit();
    }
    */
}
