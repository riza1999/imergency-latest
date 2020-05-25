package com.umn.imergency.ui.drawer.first_aid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jaeger.library.StatusBarUtil;
import com.umn.imergency.R;
import com.umn.imergency.helpers.Fetch;
import com.umn.imergency.ui.MainActivity;
import com.umn.imergency.ui.drawer.tombol_darurat.SearchResultActivity;
import com.umn.imergency.ui.drawer.tombol_darurat.SearchResultAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FirstAidDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerview_emergency_handling, recyclerview_qna;
    private RecyclerView.Adapter adapter_emergency_handling, adapter_qna;
    private RecyclerView.LayoutManager layoutmanager_emergency_handling, layoutmanager_qna;

    ImageView imageview_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        StatusBarUtil.setColor(FirstAidDetailActivity.this, Color.parseColor("#FF4081"));

        imageview_header = findViewById(R.id.imageview_header);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        getSupportActionBar().setTitle(name);

        Map<String, String> request_body = new HashMap<>();
        request_body.put("name", name);

        String URL = "https://us-central1-imergency-latest.cloudfunctions.net/QUERY_FIRST_AID_DETAIL";
        new Fetch(FirstAidDetailActivity.this, "POST", URL, request_body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray emergency_handling = response.getJSONArray("emergency_handling");
                            JSONArray qna = response.getJSONArray("qna");
                            String header_img = response.getString("header_img");

                            int resID = getResources().getIdentifier(header_img, "drawable", "com.umn.imergency");
                            imageview_header.setImageResource(resID);

                            recyclerview_emergency_handling = findViewById(R.id.recyclerview_emergency_handling);
                            layoutmanager_emergency_handling = new LinearLayoutManager(FirstAidDetailActivity.this);
                            recyclerview_emergency_handling.setLayoutManager(layoutmanager_emergency_handling);
                            adapter_emergency_handling = new PenangananDaruratAdapter(FirstAidDetailActivity.this, emergency_handling);
                            recyclerview_emergency_handling.setAdapter(adapter_emergency_handling);

                            recyclerview_qna = findViewById(R.id.recyclerview_qna);
                            layoutmanager_qna = new LinearLayoutManager(FirstAidDetailActivity.this);
                            recyclerview_qna.setLayoutManager(layoutmanager_qna);
                            adapter_qna = new QnaAdapter(FirstAidDetailActivity.this, qna);
                            recyclerview_qna.setAdapter(adapter_qna);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
