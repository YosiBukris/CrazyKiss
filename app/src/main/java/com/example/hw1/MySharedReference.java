package com.example.hw1;


import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class MySharedReference {

    private SharedPreferences prefs;

    public MySharedReference(Context context) {
        prefs = context.getSharedPreferences("MyPref", MODE_PRIVATE);

    }

    public String getString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    public void putString(String key, String value) {//save
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, String.valueOf(value));
        editor.apply();
    }

    public void removeKey(String key){
        prefs.edit().remove(key);
    }
}
