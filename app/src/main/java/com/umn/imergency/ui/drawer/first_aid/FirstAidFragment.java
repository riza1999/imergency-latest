package com.umn.imergency.ui.drawer.first_aid;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umn.imergency.R;
import com.umn.imergency.helpers.Fetch;
import com.umn.imergency.ui.drawer.tombol_darurat.SearchResultActivity;
import com.umn.imergency.ui.drawer.tombol_darurat.SearchResultAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FirstAidFragment extends Fragment {
    private RecyclerView recyclerview_firstaid;
    private RecyclerView.Adapter adapter_firstaid;
    private RecyclerView.LayoutManager layoutmanager_firstaid;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_first_aid, container, false);

        String URL = "https://us-central1-imergency-latest.cloudfunctions.net/QUERY_FIRST_AID";
        new Fetch(getContext(), "GET", URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        recyclerview_firstaid = view.findViewById(R.id.recyclerview_firstaid);
                        layoutmanager_firstaid = new LinearLayoutManager(getContext());
                        recyclerview_firstaid.setLayoutManager(layoutmanager_firstaid);
                        adapter_firstaid = new FirstAidAdapter(getContext(), response);
                        recyclerview_firstaid.setAdapter(adapter_firstaid);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                });

        return view;
    }






}
