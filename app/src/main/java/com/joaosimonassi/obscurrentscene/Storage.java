package com.joaosimonassi.obscurrentscene;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Storage {

    public static String getValue(String key, Activity activity){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public static void saveValue(String key, String value,Activity activity){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void clearStorage(Activity activity){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        sharedPref.edit().clear().apply();
    }
}
