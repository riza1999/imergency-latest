package com.umn.imergency.ui.drawer.tombol_darurat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umn.imergency.R;
import com.umn.imergency.helpers.Fetch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchResultActivity extends AppCompatActivity {
    private RecyclerView recyclerview_search_result;
    private RecyclerView.Adapter adapter_search_result;
    private RecyclerView.LayoutManager layoutmanager_search_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        enableToolbar();

        Intent intent = getIntent();
        String latitude = intent.getStringExtra("latitude");
        String longitude = intent.getStringExtra("longitude");
        String location_query = intent.getStringExtra("location_query");
        String type = intent.getStringExtra("type");

        fetchNearestLocation(latitude, longitude, type, location_query);
    }

    private void fetchNearestLocation(String latitude, String longitude, String type, String location_query) {
        final String URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?type="+type+"&query="+location_query+"&location="
                +latitude+","+longitude+"&rankby=distance&key=AIzaSyCajKAQf3yPO_kYgFvic6czN0zCwho-Jxs";

        new Fetch(SearchResultActivity.this, "GET", URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");

                            recyclerview_search_result = findViewById(R.id.recyclerview_search_result);
                            layoutmanager_search_result = new LinearLayoutManager(SearchResultActivity.this);
                            recyclerview_search_result.setLayoutManager(layoutmanager_search_result);
                            adapter_search_result = new SearchResultAdapter(results);
                            recyclerview_search_result.setAdapter(adapter_search_result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                });
    }

    private void enableToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Search Result");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
