package com.shenhui.doubanfilm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.bean.SimpleSub;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<SimpleSub> mData;
    private OnItemClickListener callback;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().
            showImageOnLoading(R.drawable.noimage).
            showImageOnFail(R.drawable.noimage).
            showImageForEmptyUri(R.drawable.lks_for_blank_url).
            cacheInMemory(true).
            cacheOnDisk(true).
            considerExifParams(true).
            build();
    private ImageLoadingListener imageLoadingListener = new AnimateFirstDisplayListener();

    public SearchAdapter(Context context, List<SimpleSub> data) {
        this.mContext = context;
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener callback) {
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_search_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        SimpleSub sub = mData.get(position);
        holder.ratingBar.setRating(((float) sub.getRating().getAverage()) / 2);
        holder.text_rating.setText(String.format("%s", sub.getRating().getAverage()));
        holder.text_collect_count.setText(String.format("%d", sub.getCollect_count()));
        holder.text_title.setText(sub.getTitle());
        if (sub.getOriginal_title().equals(sub.getTitle())) {
            holder.text_original_title.setVisibility(View.GONE);
        } else {
            holder.text_original_title.setText(sub.getOriginal_title());
        }
        holder.text_genres.setText(listToString(sub.getGenres()));
        String dir = mContext.getResources().getString(R.string.directors);
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < sub.getDirectors().size(); i++) {
            s.append(i == 0 ? "" : "/").append(sub.getDirectors().get(i).getName());
        }
        holder.text_directors.setText(dir);
        holder.text_directors.append(s.toString());
        String cast = mContext.getResources().getString(R.string.casts);
        s = new StringBuffer();
        for (int i = 0; i < sub.getCasts().size(); i++) {
            s.append(i == 0 ? "" : "/").append(sub.getCasts().get(i).getName());
        }
        holder.text_casts.setText(cast);
        holder.text_casts.append(s.toString());

        imageLoader.displayImage(sub.getImages().getLarge(),
                holder.image_film, options, imageLoadingListener);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.itemClick(position);
                }
            }
        });
    }

    private String listToString(List<String> list) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            s.append(i == 0 ? "" : "/").append(list.get(i));
        }
        return s.toString();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_search_images)
        ImageView image_film;
        @Bind(R.id.rb_search_rating)
        RatingBar ratingBar;
        @Bind(R.id.tv_search_rating)
        TextView text_rating;
        @Bind(R.id.tv_search_collect_count)
        TextView text_collect_count;
        @Bind(R.id.tv_search_title)
        TextView text_title;
        @Bind(R.id.tv_search_original_title)
        TextView text_original_title;
        @Bind(R.id.tv_search_genres)
        TextView text_genres;
        @Bind(R.id.tv_search_directors)
        TextView text_directors;
        @Bind(R.id.tv_search_casts)
        TextView text_casts;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void itemClick(int pos);
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
