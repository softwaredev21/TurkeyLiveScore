package com.xuefan.livescore.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.gson.Gson;

public class TinyDB {

    private static TinyDB instance = null;

    private SharedPreferences preferences;

    public static TinyDB getDB() {
        return instance;
    }

    public TinyDB(Context appContext) {
        try {
            preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        instance = this;
    }

    public void putInt(String key, int value) {
        if (preferences != null) {
            preferences.edit().putInt(key, value).commit();
        }
    }

    public int getInt(String key, int defValue) {
        if (preferences != null) {
            return preferences.getInt(key, defValue);
        }
        return 0;
    }

    public void putString(String key, String value) {
        if(preferences != null) {
            preferences.edit().putString(key, value).commit();
        }
    }

    public String getString(String key) {
        if(preferences != null) {
            return preferences.getString(key, "");
        }
        return null;
    }

    public Object getObject(String key, Class<?> classOfT){
        if(preferences != null) {
            String json = getString(key);
            Object value = new Gson().fromJson(json, classOfT);
            if (value == null)
                return null;
            return value;
        }
        return null;
    }

    public void putObject(String key, Object obj){
        if(key == null || key == "")
            return;
        Gson gson = new Gson();
        putString(key, gson.toJson(obj));
    }
}
