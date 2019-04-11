package com.akashapplications.technicalanna.LocalData;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserData {
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public UserData(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("Local_Preference", Activity.MODE_PRIVATE);
    }

    public boolean setLoggedIn(boolean status)
    {
        editor = preferences.edit();
        editor.putBoolean("loggedin",status);
        return editor.commit();
    }

    public boolean idLoggedIn()
    {
        return preferences.getBoolean("loggedin",false);
    }

    public boolean setName(String name)
    {
        editor = preferences.edit();
        editor.putString("name",name);
        return editor.commit();
    }

    public String getName()
    {
        return preferences.getString("name", null);
    }

    public boolean setEmail(String email)
    {
        editor = preferences.edit();
        editor.putString("email",email);
        return editor.commit();
    }

    public String getEmail()
    {
        return preferences.getString("email", null);
    }

    public boolean setPhone(String phone)
    {
        editor = preferences.edit();
        editor.putString("phone",phone);
        return editor.commit();
    }

    public String getPhone()
    {
        return preferences.getString("phone", null);
    }

    public boolean setImage(String image)
    {
        editor = preferences.edit();
        editor.putString("image",image);
        return editor.commit();
    }

    public String getImage()
    {
        return preferences.getString("image", null);
    }

    public boolean setUserID(String userid)
    {
        editor = preferences.edit();
        editor.putString("userid",userid);
        return editor.commit();
    }

    public String getUserID()
    {
        return preferences.getString("userid", null);
    }

    public boolean setPhoneVerified(boolean phoneStatus)
    {
        editor = preferences.edit();
        editor.putBoolean("phoneStatus",phoneStatus);
        return editor.commit();
    }

    public boolean isPhoneVerified()
    {
        return preferences.getBoolean("phoneStatus",false);
    }

    public boolean setEmailVerified(boolean emailStatus)
    {
        editor = preferences.edit();
        editor.putBoolean("emailStatus",emailStatus);
        return editor.commit();
    }

    public boolean isEmailVerified()
    {
        return preferences.getBoolean("emailStatus",false);
    }

    public void logOut()
    {
        editor = preferences.edit();
        editor.clear().commit();
        new EmailOTP(context).deleteAllPref();
    }

}
