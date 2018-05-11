package com.automata.zalego_news;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by HANSEN on 11/05/2018.
 */

public class ZalegoNews extends Application {
    private RequestQueue mRequestQueue;
    public static final String TAG = ZalegoNews.class
            .getSimpleName();
    private static ZalegoNews mInstance;
    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

    }
    public static synchronized ZalegoNews getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

}
