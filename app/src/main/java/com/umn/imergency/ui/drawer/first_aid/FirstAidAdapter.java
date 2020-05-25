package com.umn.imergency.ui.drawer.first_aid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.umn.imergency.R;
import com.umn.imergency.ui.drawer.tombol_darurat.InstanceDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FirstAidAdapter extends RecyclerView.Adapter<FirstAidAdapter.MyViewHolder> {
    private Context context;
    private JSONObject results;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Elemen elemen yang ada di item
        public TextView disease_name;
        public ImageView icon;
        public CardView cardview_result;

        public MyViewHolder(View view) {
            super(view);
            disease_name = view.findViewById(R.id.name);
            icon = view.findViewById(R.id.icon);
            cardview_result = view.findViewById(R.id.cardview_result);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FirstAidAdapter(Context context, JSONObject results) {
        this.context = context;
        this.results = results;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Layout individu nya
        View contactView = inflater.inflate(R.layout.item_firstaid, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(contactView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            JSONArray response = results.getJSONArray("diseases");
            JSONObject result = response.getJSONObject(position);
            String nama_icon = result.getString("icon_img");
            int resID = context.getResources().getIdentifier(nama_icon, "drawable", "com.umn.imergency");
            final String name = result.getString("name");

            holder.disease_name.setText(name);
            holder.icon.setImageResource(resID);

            holder.cardview_result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FirstAidDetailActivity.class);
                    intent.putExtra("name", name);
                    context.startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        int length = 0;

        try {
            JSONArray response = results.getJSONArray("diseases");
            length = response.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
       return length;
    }
}
