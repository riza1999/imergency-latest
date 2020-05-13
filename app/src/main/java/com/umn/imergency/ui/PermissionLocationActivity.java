package com.umn.imergency.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.umn.imergency.R;

public class PermissionLocationActivity extends AppCompatActivity {

    private Button button_request_location_permission, button_finish_permission;

    private int REQUEST_FINE_LOCATION = 1, REQUEST_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_location);

        button_request_location_permission = findViewById(R.id.button_request_location_permission);
        button_finish_permission = findViewById(R.id.button_finish_permission);

        // Check permission first time
        if(ActivityCompat.checkSelfPermission(PermissionLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(PermissionLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(
                    PermissionLocationActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_FINE_LOCATION);
            ActivityCompat.requestPermissions(
                    PermissionLocationActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_COARSE_LOCATION);
        }
        else{
            Toast.makeText(PermissionLocationActivity.this, "You Already Enabled Location Permission", Toast.LENGTH_SHORT).show();
        }

        button_request_location_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(PermissionLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(PermissionLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(
                            PermissionLocationActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_FINE_LOCATION);
                    ActivityCompat.requestPermissions(
                            PermissionLocationActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_COARSE_LOCATION);
                }
                else{
                    Toast.makeText(PermissionLocationActivity.this, "You Already Enabled Location Permission", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_finish_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(PermissionLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(PermissionLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    Intent startNewActivity = new Intent(PermissionLocationActivity.this, MainActivity.class);
                    startActivity(startNewActivity);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
                else{
                    Toast.makeText(PermissionLocationActivity.this, "You Need to Enabled Location Permission", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
