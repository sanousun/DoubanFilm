package com.shenhui.doubanfilm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
 * Created by sanousun on 2015/9/3.
 */
public class SimSubAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOT = 1;

    private Context mContext;
    private List<SimpleSub> mData;
    private OnItemClickListener callback;

    /**
     * 用于加载更多数据
     */
    private int start = 0;
    private int total = Integer.MAX_VALUE;
    /**
     * 判断是否属于“即将上映”
     */
    private boolean isComing;

    private static final int DEFAULT_COUNT = 20;

    public SimSubAdapter(Context context, List<SimpleSub> data) {
        this(context, data, false);
    }

    public SimSubAdapter(Context context, List<SimpleSub> data, boolean isComing) {
        this.mContext = context;
        this.mData = data;
        this.isComing = isComing;
    }

    /**
     * 用于加载更多数据时的url起点
     */
    public int getStart() {
        start += DEFAULT_COUNT;
        return start;
    }

    /**
     * 返回adapter数据的总数
     */
    public int getTotal() {
        return total;
    }

    /**
     * 判断是否已经加载完毕
     */
    public boolean loadCompleted() {
        return mData.size() == total;
    }

    public void setOnItemClickListener(OnItemClickListener callback) {
        this.callback = callback;
    }

    public void loadMoreData(List<SimpleSub> data) {
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    public void updateList(List<SimpleSub> data, int total) {
        this.mData = data;
        this.total = total;
        notifyDataSetChanged();
    }

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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOT) {
            View view = LayoutInflater.from(mContext).inflate
                    (R.layout.view_load_tips, parent, false);
            return new FootViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate
                    (R.layout.item_simple_sub_layout, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (position == mData.size()) {
            if (position == 0) {
                ((FootViewHolder) viewHolder).itemView.setVisibility(View.GONE);
            } else if (loadCompleted()) {
                ((FootViewHolder) viewHolder).progressBar.setVisibility(View.GONE);
                ((FootViewHolder) viewHolder).tip.setText("加载完毕！");
            } else {
                ((FootViewHolder) viewHolder).itemView.setVisibility(View.VISIBLE);
            }
            return;
        }
        SimpleSub sub = mData.get(position);
        ItemViewHolder holder = (ItemViewHolder) viewHolder;
        if (!isComing) {
            holder.rating.setVisibility(View.VISIBLE);
            float rate = (float) sub.getRating().getAverage();
            holder.ratingBar.setRating(rate / 2);
            holder.text_rating.setText("" + rate);
            holder.collect_count.setText(sub.getCollect_count() + "");
        }
        String title = sub.getTitle();
        String original_title = sub.getOriginal_title();
        holder.title.setText(title);
        if (original_title.equals(title)) {
            holder.original_title.setVisibility(View.GONE);
        } else {
            holder.original_title.setText(original_title);
            holder.original_title.setVisibility(View.VISIBLE);
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < sub.getGenres().size(); i++) {
            stringBuffer.append(i == 0 ? "" : ",");
            stringBuffer.append(sub.getGenres().get(i));
        }
        holder.genres.setText(stringBuffer.toString());
        stringBuffer.delete(0, stringBuffer.length());
        for (int i = 0; i < sub.getDirectors().size(); i++) {
            stringBuffer.append(i == 0 ? "" : "/");
            stringBuffer.append(sub.getDirectors().get(i).getName());
        }
        holder.directors.setText(stringBuffer.toString());
        stringBuffer.delete(0, stringBuffer.length());
        for (int i = 0; i < sub.getCasts().size(); i++) {
            stringBuffer.append(i == 0 ? "" : "/");
            stringBuffer.append(sub.getCasts().get(i).getName());
        }
        holder.casts.setText(stringBuffer.toString());
        imageLoader.displayImage(sub.getImages().getLarge(),
                holder.image, options, imageLoadingListener);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.itemClick(mData.get(position).getId());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mData.size()) {
            return TYPE_FOOT;
        }
        return TYPE_ITEM;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private LinearLayout rating;
        private RatingBar ratingBar;
        private TextView text_rating;
        private TextView collect_count;
        private TextView title;
        private TextView original_title;
        private TextView genres;
        private TextView directors;
        private TextView casts;

        public ItemViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.iv_coming_images);
            rating = (LinearLayout) itemView.findViewById(R.id.ll_coming_rating);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rb_coming_rating);
            text_rating = (TextView) itemView.findViewById(R.id.tv_coming_rating);
            collect_count = (TextView) itemView.findViewById(R.id.tv_coming_collect_count);
            title = (TextView) itemView.findViewById(R.id.tv_coming_title);
            original_title = (TextView) itemView.findViewById(R.id.tv_coming_original_title);
            genres = (TextView) itemView.findViewById(R.id.tv_coming_genres);
            directors = (TextView) itemView.findViewById(R.id.tv_coming_director);
            casts = (TextView) itemView.findViewById(R.id.tv_coming_casts1);
        }
    }

    /**
     * recyclerView上拉加载更多的footViewHolder
     */
    class FootViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;
        private TextView tip;

        public FootViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pb_tip);
            tip = (TextView) itemView.findViewById(R.id.tv_tip);
        }
    }

    public interface OnItemClickListener {
        void itemClick(String id);
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
