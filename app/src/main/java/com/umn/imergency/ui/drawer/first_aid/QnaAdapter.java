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

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.umn.imergency.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QnaAdapter extends RecyclerView.Adapter<QnaAdapter.MyViewHolder> {
    private Context context;
    private JSONArray results;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Elemen elemen yang ada di item
        public TextView textview_title;
        public ExpandableTextView expandabletextview;

        public MyViewHolder(View view) {
            super(view);
            textview_title = view.findViewById(R.id.textview_title);
            expandabletextview = view.findViewById(R.id.expand_text_view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public QnaAdapter(Context context, JSONArray results) {
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
        View contactView = inflater.inflate(R.layout.item_qna, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(contactView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d("<<<", results.toString());
        try {
            JSONObject result = results.getJSONObject(position);
            String title = result.getString("name").replaceAll("\\\\n", "\n");
            String caption = result.getString("detail").replaceAll("\\\\n", "\n");

            holder.textview_title.setText(title);
            holder.expandabletextview.setText(caption);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        Log.d("<<<<", String.valueOf(results.length()));
        return results.length();
    }
}
