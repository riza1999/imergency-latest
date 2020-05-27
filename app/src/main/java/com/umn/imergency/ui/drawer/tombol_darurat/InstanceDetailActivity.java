package com.umn.imergency.ui.drawer.tombol_darurat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.umn.imergency.R;
import com.umn.imergency.helpers.Fetch;

import org.json.JSONException;
import org.json.JSONObject;

public class InstanceDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap google_map;

    private TextView textview_name, textview_address, textview_phone_number;
    private Button button_call;
    private ConstraintLayout container_instance_detail;
    private ProgressBar progressbar_instance_detail;
    private FloatingActionButton fab_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instance_detail);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        hideSystemUI();

        textview_name = findViewById(R.id.textview_name);
        textview_address = findViewById(R.id.textview_address);
        textview_phone_number = findViewById(R.id.textview_phone_number);
        button_call = findViewById(R.id.button_call);
        container_instance_detail = findViewById(R.id.container_instance_detail);
        progressbar_instance_detail = findViewById(R.id.progressbar_instance_detail);
        fab_back = findViewById(R.id.fab_back);

        fab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            hideSystemUI();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.google_map = googleMap;

        Intent intent = getIntent();
        String place_id = intent.getStringExtra("place_id");
        final String name = intent.getStringExtra("name");
        final String address = intent.getStringExtra("address");

        String URL = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+place_id+
                "&key=AIzaSyCajKAQf3yPO_kYgFvic6czN0zCwho-Jxs";
        Log.d("URL>>>", URL);
        new Fetch(InstanceDetailActivity.this, "GET", URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(">>>>", response.toString());
                        try {
                            JSONObject result = response.getJSONObject("result");
                            JSONObject geometry = result.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
                            String latitude = location.getString("lat");
                            String longitude = location.getString("lng");

                            if (result.has("international_phone_number")) {
                                String phone_number = result.getString("international_phone_number");
                                setInstanceDetailInfo(name, address, phone_number);
                            } else {
                                setInstanceDetailInfo(name, address);
                            }

                            LatLng instance_location = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                            google_map.setPadding(0,45,0,0);
                            google_map.addMarker(new MarkerOptions().position(instance_location).title(name));
                            google_map.moveCamera(CameraUpdateFactory.newLatLngZoom(instance_location, 15));
                            google_map.setMyLocationEnabled(true);

                            showContainerInstanceDetail();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InstanceDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // If the phone number exist
    private void setInstanceDetailInfo(String name, String address, final String phone_number) {
        textview_name.setText(name);
        textview_address.setText(address);
        textview_phone_number.setText(phone_number);

        button_call.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission") // Sudah di halaman utama
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse("tel:"+phone_number));
                startActivity(callIntent);
            }
        });
    }

    // If the phone number doesn't exist
    private void setInstanceDetailInfo(String name, String address) {
        textview_name.setText(name);
        textview_address.setText(address);

        button_call.setClickable(false);
        button_call.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
    }

    private void showContainerInstanceDetail() {
        container_instance_detail.setVisibility(ConstraintLayout.VISIBLE);
        progressbar_instance_detail.setVisibility(ProgressBar.GONE);
    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        //                      | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
