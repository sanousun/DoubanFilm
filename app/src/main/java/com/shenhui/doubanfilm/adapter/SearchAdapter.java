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

/**
 * Created by Sanousun on 2015/8/24.
 */
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

    public void searchChange(List<SimpleSub> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener callback) {
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_search_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        SimpleSub sub = mData.get(position);
        holder.ratingBar.setRating(((float) sub.getRating().getAverage()) / 2);
        holder.text_rating.setText(sub.getRating().getAverage() + "");
        holder.text_collect_count.setText(sub.getCollect_count() + "");
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
            s.append((i == 0 ? "" : "/") + sub.getDirectors().get(i).getName());
        }
        holder.text_directors.setText(dir);
        holder.text_directors.append(s.toString());
        String cast = mContext.getResources().getString(R.string.casts);
        s = new StringBuffer();
        for (int i = 0; i < sub.getCasts().size(); i++) {
            s.append((i == 0 ? "" : "/") + sub.getCasts().get(i).getName());
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
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            s.append((i == 0 ? "" : "/") + list.get(i));
        }
        return s.toString();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_film;
        public RatingBar ratingBar;
        public TextView text_rating;
        public TextView text_collect_count;
        public TextView text_title;
        public TextView text_original_title;
        public TextView text_genres;
        public TextView text_directors;
        public TextView text_casts;

        public ViewHolder(View itemView) {
            super(itemView);
            image_film = (ImageView) itemView.findViewById(R.id.iv_search_images);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rb_search_rating);
            text_rating = (TextView) itemView.findViewById(R.id.tv_search_rating);
            text_collect_count = (TextView) itemView.findViewById(R.id.tv_search_collect_count);
            text_title = (TextView) itemView.findViewById(R.id.tv_search_title);
            text_original_title = (TextView) itemView.findViewById(R.id.tv_search_original_title);
            text_genres = (TextView) itemView.findViewById(R.id.tv_search_genres);
            text_directors = (TextView) itemView.findViewById(R.id.tv_search_directors);
            text_casts = (TextView) itemView.findViewById(R.id.tv_search_casts);
        }
    }

    public interface OnItemClickListener {
        public void itemClick(int pos);
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
