package com.shenhui.doubanfilm.app;

import android.app.Application;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.db.DataSource;
import com.shenhui.doubanfilm.support.OkHttpStack;
import com.squareup.leakcanary.LeakCanary;

import java.sql.SQLException;

public class MyApplication extends Application {

    private static DisplayImageOptions mLoaderOptions;
    private static RequestQueue mQueue;
    private static DataSource mSource;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        initImageLoader(getApplicationContext());
        mQueue = Volley.newRequestQueue(getApplicationContext(), new OkHttpStack());
        mSource = new DataSource(getApplicationContext());
        try {
            mSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.
                Builder(context).
                denyCacheImageMultipleSizesInMemory().
                threadPriority(Thread.NORM_PRIORITY - 2).
                diskCacheFileNameGenerator(new Md5FileNameGenerator()).
                tasksProcessingOrder(QueueProcessingType.FIFO).
                build();
        ImageLoader.getInstance().init(config);
        mLoaderOptions = new DisplayImageOptions.Builder().
                showImageOnLoading(R.drawable.no_image).
                showImageOnFail(R.drawable.no_image).
                showImageForEmptyUri(R.drawable.no_image).
                imageScaleType(ImageScaleType.EXACTLY_STRETCHED).
                cacheInMemory(true).
                cacheOnDisk(true).
                considerExifParams(true).
                build();
    }

    public static DataSource getDataSource() {
        return mSource;
    }

    public static RequestQueue getHttpQueue() {
        return mQueue;
    }

    public static DisplayImageOptions getLoaderOptions() {
        return mLoaderOptions;
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
