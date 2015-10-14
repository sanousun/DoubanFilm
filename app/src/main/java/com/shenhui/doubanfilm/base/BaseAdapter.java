package com.shenhui.doubanfilm.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.support.AnimateFirstDisplayListener;

/**
 * Created by sanousun on 2015/10/14.
 */
public class BaseAdapter<T extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<T> {

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    protected DisplayImageOptions options = new DisplayImageOptions.Builder().
            showImageOnLoading(R.drawable.noimage).
            showImageOnFail(R.drawable.noimage).
            showImageForEmptyUri(R.drawable.lks_for_blank_url).
            cacheInMemory(true).
            cacheOnDisk(true).
            considerExifParams(true).
            build();
    protected AnimateFirstDisplayListener imageLoaderListener =
            new AnimateFirstDisplayListener();

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(T holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
