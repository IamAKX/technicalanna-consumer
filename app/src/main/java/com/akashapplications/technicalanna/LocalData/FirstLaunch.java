package com.akashapplications.technicalanna.LocalData;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class FirstLaunch {
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public FirstLaunch(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("EmailOTP", Activity.MODE_PRIVATE);
    }

    public boolean setFirstTime(boolean status)
    {
        editor = preferences.edit();
        editor.putBoolean("status",status);
        return editor.commit();
    }
    public boolean idFirstTime()
    {
        return preferences.getBoolean("status",true);
    }

}
