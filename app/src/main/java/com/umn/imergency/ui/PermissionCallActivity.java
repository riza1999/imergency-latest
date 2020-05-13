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

public class PermissionCallActivity extends AppCompatActivity {
    private Button button_request_call_permission, button_next_permission;

    private int REQUEST_CALL_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_call);

        button_request_call_permission = findViewById(R.id.button_request_call_permission);
        button_next_permission = findViewById(R.id.button_next_permission);

        // Check permission first time
        if(ActivityCompat.checkSelfPermission(PermissionCallActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    PermissionCallActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CALL_PERMISSION);
        }
        else{
            Toast.makeText(PermissionCallActivity.this, "You Already Enabled Call Permission", Toast.LENGTH_SHORT).show();
        }

        button_request_call_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(PermissionCallActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(
                            PermissionCallActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_CALL_PERMISSION);
                }
                else{
                    Toast.makeText(PermissionCallActivity.this, "You Already Enabled Call Permission", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_next_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(PermissionCallActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(PermissionCallActivity.this, PermissionLocationActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(PermissionCallActivity.this, "You Need to Enabled Call Permission", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
