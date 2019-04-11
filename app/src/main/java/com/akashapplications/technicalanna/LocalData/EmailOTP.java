package com.akashapplications.technicalanna.LocalData;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.concurrent.TimeUnit;

public class EmailOTP {
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public EmailOTP(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("EmailOTP", Activity.MODE_PRIVATE);
    }

    public boolean setPin(String pin)
    {
        editor = preferences.edit();
        editor.putString("pin",pin);
        editor.putLong("expiry",System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
        return editor.commit();
    }
    public String getPin()
    {
        return preferences.getString("pin",null);
    }


    public boolean isPinValid()
    {
        if(getPin() == null)
            return false;
        if(System.currentTimeMillis() > preferences.getLong("expiry", -1))
        {
            editor.clear();
            editor.commit();
            return false;
        }
        return true;
    }

    public void deleteAllPref()
    {
        editor = preferences.edit();
        editor.clear().commit();
    }

}
