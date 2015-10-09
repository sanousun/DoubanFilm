package com.shenhui.doubanfilm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.bean.CastAndCommend;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sanousun on 2015/9/4.
 */
public class SubCardAdapter extends RecyclerView.Adapter<SubCardAdapter.ViewHolder> {

    private Context mContext;
    private List<CastAndCommend> mData;
    private OnItemClickListener callback;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().
            showImageForEmptyUri(R.drawable.noimage).
            showImageOnFail(R.drawable.noimage).
            showImageForEmptyUri(R.drawable.lks_for_blank_url).
            cacheInMemory(true).
            cacheOnDisk(true).
            considerExifParams(true).
            build();
    private ImageLoadingListener imageLoadingListener = new AnimateFirstDisplayListener();

    public SubCardAdapter(Context context, List<CastAndCommend> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_subject_layout, parent, false);
        return new ViewHolder(v);
    }

    public void setOnItemClickListener(OnItemClickListener callback) {
        this.callback = callback;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CastAndCommend sub = mData.get(position);
        imageLoader.displayImage(sub.getMedium(),
                holder.image, options, imageLoadingListener);
        if (sub.getIsDir()) {
            holder.text_dir.setVisibility(View.VISIBLE);
        }
        holder.text.setText(sub.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.itemClick(mData.get(position).getId(), mData.get(position).getIsCom());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView text;
        private TextView text_dir;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.sub_item_image);
            text = (TextView) itemView.findViewById(R.id.sub_item_text);
            text_dir = (TextView) itemView.findViewById(R.id.sub_dir_text);
        }
    }

    public interface OnItemClickListener {
        public void itemClick(String id, boolean isCom);
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
