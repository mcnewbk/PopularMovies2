package com.mcnew.brandon.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Brandon on 3/1/2016.
 */
public class Utils {

    private static String POPULAR_MOVIES_PREFERENCES = "popular_movies_preferences";
    public static Boolean GetPreferenceBoolean(Context context, String key){
        SharedPreferences localStorage = context.getSharedPreferences(POPULAR_MOVIES_PREFERENCES, Context.MODE_MULTI_PROCESS);
        return localStorage.getBoolean(key, false);
    }

    public static void StorePreference(Context context, String key, Boolean value) {
        // store token in shared preferences
        SharedPreferences localStorage = context.getSharedPreferences(POPULAR_MOVIES_PREFERENCES, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = localStorage.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static ArrayList<String> GetAllFavorites(Context context){
        SharedPreferences localStorage = context.getSharedPreferences(POPULAR_MOVIES_PREFERENCES, Context.MODE_MULTI_PROCESS);
        Map<String, ?> all =  localStorage.getAll();
        ArrayList<String> ret = new ArrayList<String>();
        for(String key : all.keySet()){
            if(localStorage.getBoolean(key, false)){
                ret.add(key);
            }
        }
        return ret;
    }
}
