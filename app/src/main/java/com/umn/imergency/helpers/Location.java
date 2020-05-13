package com.umn.imergency.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class Location {
    private Context context;
    private FusedLocationProviderClient fusedLocationClient;

    private Map<String, String> result = new HashMap<>();

    protected Location(Context context) {
        this.context = context;

        if(this.isLocationEnabled()) {
            this.retrieveLastLocation();
        } else {
            Toast.makeText(this.context, "Mohon nyalakan GPS", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            this.context.startActivity(intent);
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (android.location.LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                android.location.LocationManager.NETWORK_PROVIDER
        );
    }

    private void retrieveLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context);
        fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
            @Override
            public void onComplete(@NonNull Task<android.location.Location> task) {
                // Karena nama class sama, maka jadi android.location.Location
                android.location.Location location = task.getResult();

                if (location == null) {
                    requestNewLocationData();
                    onLocationReceived(result);
                } else {
                    String latitude = Double.toString(location.getLatitude()) ;
                    String longitude = Double.toString(location.getLongitude());

                    result.put("latitude", latitude);
                    result.put("longitude", longitude);

                    Log.d(">>>!", latitude+" "+longitude);
                    onLocationReceived(result);
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(0);
        locationRequest.setNumUpdates(1);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context);
        fusedLocationClient.requestLocationUpdates(
                locationRequest, locationCallback,
                Looper.myLooper()
        );
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            android.location.Location location = locationResult.getLastLocation();
            String latitude = Double.toString(location.getLatitude()) ;
            String longitude = Double.toString(location.getLongitude());

            result.put("latitude", latitude);
            result.put("longitude", longitude);
        }
    };

    // Untuk ambil locationnya, Override method ini
    public void onLocationReceived(Map<String, String> location) {}
}
