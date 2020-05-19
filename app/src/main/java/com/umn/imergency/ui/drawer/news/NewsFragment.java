package com.umn.imergency.ui.drawer.news;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umn.imergency.R;
import com.umn.imergency.helpers.Fetch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NewsFragment extends Fragment {
    String API_KEY = "8c42a2ee712146cb957b91ca0dce1f7a"; // ### YOUE NEWS API HERE ###
    String CATEGORY = "health";
    String COUNTRY = "id";
    ListView listNews;
    ProgressBar loader;

    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    static final String KEY_AUTHOR = "author";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_URL = "url";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_PUBLISHEDAT = "publishedAt";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_news, container, false);

        //News Stuff
        listNews = (ListView) view.findViewById(R.id.listNews);
        loader = (ProgressBar) view.findViewById(R.id.loader);
        listNews.setEmptyView(loader);

        String URL = "https://newsapi.org/v2/top-headlines?country="+COUNTRY+"&category="+CATEGORY+"&apiKey="+API_KEY;

        new Fetch(getActivity(), "GET", URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Integer newsCount = response.optInt("totalResults");
                            if(response.length() != 0 || newsCount != 0){
                                JSONArray jsonArray = response.optJSONArray("articles");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    JSONObject source = jsonObject.getJSONObject("source");
                                    String name = source.optString("name");

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put(KEY_AUTHOR, name);
                                    map.put(KEY_TITLE, jsonObject.optString(KEY_TITLE).toString());
                                    map.put(KEY_DESCRIPTION, jsonObject.optString(KEY_DESCRIPTION).toString());
                                    map.put(KEY_URL, jsonObject.optString(KEY_URL).toString());
                                    map.put(KEY_URLTOIMAGE, jsonObject.optString(KEY_URLTOIMAGE).toString());
                                    map.put(KEY_PUBLISHEDAT, jsonObject.optString(KEY_PUBLISHEDAT).toString());
                                    dataList.add(map);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ListNewsAdapter adapter = new ListNewsAdapter(getActivity(), dataList);
                        listNews.setAdapter(adapter);

                        listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                Intent i = new Intent(getActivity(), NewsDetail.class);
                                i.putExtra("url", dataList.get(+position).get(KEY_URL));
                                i.putExtra("title", dataList.get(+position).get(KEY_TITLE));
                                startActivity(i);
                            }
                        });
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
