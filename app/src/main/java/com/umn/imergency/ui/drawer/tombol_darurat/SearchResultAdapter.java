package com.umn.imergency.ui.drawer.tombol_darurat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.umn.imergency.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyViewHolder> {
    private JSONArray results;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Elemen elemen yang ada di item
        public TextView textview_name, textview_address, textview_rating;

        public MyViewHolder(View view) {
            super(view);
            textview_name = view.findViewById(R.id.textview_name);
            textview_address = view.findViewById(R.id.textview_address);
            textview_rating = view.findViewById(R.id.textview_rating);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchResultAdapter(JSONArray results) {
        this.results = results;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SearchResultAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_search_result, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(contactView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            JSONObject result = results.getJSONObject(position);
            String name = result.getString("name");
            String address = result.getString("formatted_address");
            double rating = result.getDouble("rating");

            holder.textview_name.setText(name);
            holder.textview_address.setText(address);
            holder.textview_rating.setText(Double.toString(rating));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.results.length();
    }
}
