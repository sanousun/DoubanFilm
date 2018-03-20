package com.shenhui.doubanfilm.app;

import android.app.Application;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.shenhui.doubanfilm.db.DataSource;
import com.squareup.leakcanary.LeakCanary;

import java.sql.SQLException;

public class MyApplication extends Application {

    private static RequestQueue mQueue;
    private static DataSource mSource;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        mQueue = Volley.newRequestQueue(getApplicationContext(), new OkHttpStack());
        mSource = new DataSource(getApplicationContext());
        try {
            mSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DataSource getDataSource() {
        return mSource;
    }

    public static RequestQueue getHttpQueue() {
        return mQueue;
    }

    public static void addRequest(Request request, Object tag) {
        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(request);
    }

    public static void removeRequest(Object tag) {
        mQueue.cancelAll(tag);
    }

}
