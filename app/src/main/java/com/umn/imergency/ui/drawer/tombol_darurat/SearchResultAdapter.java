package com.umn.imergency.ui.drawer.tombol_darurat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.umn.imergency.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyViewHolder> {
    private Context context;
    private JSONArray results;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Elemen elemen yang ada di item
        public TextView textview_name, textview_address, textview_rating, textview_open_now;
        public CardView cardview_result;

        public MyViewHolder(View view) {
            super(view);
            textview_name = view.findViewById(R.id.textview_name);
            textview_address = view.findViewById(R.id.textview_address);
            textview_rating = view.findViewById(R.id.textview_rating);
            textview_open_now = view.findViewById(R.id.textview_open_now);
            cardview_result = view.findViewById(R.id.cardview_result);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchResultAdapter(Context context, JSONArray results) {
        this.context = context;
        this.results = results;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SearchResultAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Layout individu nya
        View contactView = inflater.inflate(R.layout.item_search_result, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(contactView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            JSONObject result = results.getJSONObject(position);
            final String place_id = result.getString("place_id");
            final String name = result.getString("name");
            final String address = result.getString("formatted_address");
            double rating = result.getDouble("rating");

            if(result.has("opening_hours")) {
                JSONObject opening_hours = result.getJSONObject("opening_hours");
                if(opening_hours.has("open_now")) {
                    boolean open_now = opening_hours.getBoolean("open_now");
                    if(open_now) {
                        holder.textview_open_now.setText("Open Now: Yes");
                        holder.textview_open_now.setTextColor(Color.parseColor("#00C853"));
                    }
                    else {
                        holder.textview_open_now.setText("Open Now: No");
                        holder.textview_open_now.setTextColor(Color.parseColor("#D50000"));
                    }
                }
            }
            holder.textview_name.setText(name);
            holder.textview_address.setText(address);
            holder.textview_rating.setText("Rating: "+rating);

            holder.cardview_result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, InstanceDetailActivity.class);
                    intent.putExtra("place_id", place_id);
                    intent.putExtra("name", name);
                    intent.putExtra("address", address);

                    context.startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.results.length();
    }
}
