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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PenangananDaruratAdapter extends RecyclerView.Adapter<PenangananDaruratAdapter.MyViewHolder> {
    private Context context;
    private JSONArray results;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Elemen elemen yang ada di item
        public TextView textview_index, textview_caption;

        public MyViewHolder(View view) {
            super(view);
            textview_index = view.findViewById(R.id.textview_index);
            textview_caption = view.findViewById(R.id.textview_caption);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PenangananDaruratAdapter(Context context, JSONArray results) {
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
        View contactView = inflater.inflate(R.layout.item_penanganan_darurat, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(contactView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Log.d(">>>", results.toString());
        try {
            String cara = results.getString(position).replaceAll("\\\\n", "\n");;

            holder.textview_index.setText(String.valueOf(position+1));
            holder.textview_caption.setText(cara);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return results.length();
    }
}
