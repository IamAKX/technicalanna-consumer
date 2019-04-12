package com.akashapplications.technicalanna;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.akashapplications.technicalanna.LocalData.FirstLaunch;
import com.akashapplications.technicalanna.LocalData.UserData;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(new FirstLaunch(getBaseContext()).idFirstTime()) {
                            new FirstLaunch(getBaseContext()).setFirstTime(false);
                            startActivity(new Intent(getBaseContext(), AppIntro.class));
                            finish();
                        }
                        else
                            if(new UserData(getBaseContext()).idLoggedIn())
                            {
                                startActivity(new Intent(getBaseContext(), MainContainer.class));
                                finish();
                            }
                            else {
                                startActivity(new Intent(getBaseContext(), Login.class));
                                finish();
                            }
                    }
                });
            }
        }).start();
    }
}
