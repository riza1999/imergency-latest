package com.umn.imergency;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jaeger.library.StatusBarUtil;

public class SplashActivity extends AppCompatActivity {
    /*
        Shared Preference keys:
        isLogin: boolean = Check if the user is already logged in or not
        isFinishPermission: boolean = Check if the user finish the permission challenge
     */

    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // TO-DO: Check if the user logged in or not

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent startNewActivity = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(startNewActivity);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
