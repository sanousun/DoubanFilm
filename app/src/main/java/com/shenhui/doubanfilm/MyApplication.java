package com.shenhui.doubanfilm;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.shenhui.doubanfilm.db.FilmDataSource;
import com.shenhui.doubanfilm.support.OkHttpStack;
import com.squareup.leakcanary.LeakCanary;

import java.sql.SQLException;

/**
 * Created by sanousun on 2015/8/15.
 */
public class MyApplication extends Application {

    private static MyApplication myApplicationContext;
    private static RequestQueue mQueue;
    private static FilmDataSource mSource;

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).
                denyCacheImageMultipleSizesInMemory().
                threadPriority(Thread.NORM_PRIORITY - 2).
                diskCacheFileNameGenerator(new Md5FileNameGenerator()).
                tasksProcessingOrder(QueueProcessingType.FIFO).
                build();
        ImageLoader.getInstance().init(config);
    }

    public static MyApplication getInstance() {
        return myApplicationContext;
    }

    public static RequestQueue getHttpQueue() {
        return mQueue;
    }

    public static FilmDataSource getDataSource() {
        return mSource;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        myApplicationContext = this;
        initImageLoader(getApplicationContext());
        mQueue = Volley.newRequestQueue(getApplicationContext(), new OkHttpStack());
        mSource = new FilmDataSource(getApplicationContext());
        try {
            mSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
