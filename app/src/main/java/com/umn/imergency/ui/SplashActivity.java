package com.umn.imergency.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.umn.imergency.R;

public class SplashActivity extends AppCompatActivity {
    /*
        For shared preference keys, see string.xml
     */
    final int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("imergency", Context.MODE_PRIVATE);
                Boolean is_logged_in = sharedPreferences.getBoolean(getString(R.string.sp_key_is_logged_in), false);
                Boolean is_finish_permission = sharedPreferences.getBoolean(getString(R.string.sp_key_is_finish_permission), false);

                if(!is_logged_in) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                } else if(!is_finish_permission) {
                    Intent intent = new Intent(SplashActivity.this, PermissionCallActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
