package com.umn.imergency.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.util.Log;
import android.util.LruCache;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;


public class Fetch {
    public Fetch(Context context, String method, String URL, @Nullable Map<String, String> request_body, Response.Listener<JSONObject> listener,
                 @Nullable Response.ErrorListener errorListener) {
        JSONObject request_body_to_send = new JSONObject();

        if (request_body != null) {
            request_body_to_send = new JSONObject(request_body);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                resolveNetworkMethod(method),
                URL,
                request_body_to_send,
                listener,
                errorListener
        );

        NetworkSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private int resolveNetworkMethod(String method) {
        switch (method) {
            case "POST": {
                return 1;
            }
            case "GET": {
                return 0;
            }
            default: {
                return 99;
            }
        }
    }
}

class NetworkSingleton {
    private static NetworkSingleton instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context ctx;

    private NetworkSingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue,
            new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap>
                        cache = new LruCache<String, Bitmap>(20);

                @Override
                public Bitmap getBitmap(String url) {
                    return cache.get(url);
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    cache.put(url, bitmap);
                }
            });
    }

    public static synchronized NetworkSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkSingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}