package com.shenhui.doubanfilm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.base.BaseAdapter;
import com.shenhui.doubanfilm.bean.SimpleSub;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SimSubAdapter extends BaseAdapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOT = 1;
    private static final int DEFAULT_COUNT = 20;

    private Context mContext;
    private List<SimpleSub> mData;
    /**
     * 用于加载更多数据
     */
    private int start = 0;
    private int total = Integer.MAX_VALUE;
    /**
     * 判断是否属于“即将上映”
     */
    private boolean isComing;

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

    public void loadMoreData(List<SimpleSub> data) {
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    public void updateList(List<SimpleSub> data, int total) {
        this.mData = data;
        this.total = total;
        notifyDataSetChanged();
    }

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
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (position == mData.size()) {
            showFootView(((FootViewHolder) viewHolder), position);
            return;
        }
        SimpleSub sub = mData.get(position);
        ItemViewHolder holder = (ItemViewHolder) viewHolder;
        if (!isComing) {
            holder.rating.setVisibility(View.VISIBLE);
            float rate = (float) sub.getRating().getAverage();
            holder.ratingBar.setRating(rate / 2);
            holder.text_rating.setText(String.format("%s", rate));
            holder.collect_count.setText(
                    String.format("%s%d%s",
                            mContext.getString(R.string.collect),
                            sub.getCollect_count(),
                            mContext.getString(R.string.count)));
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
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < sub.getGenres().size(); i++) {
            str.append(i == 0 ? "" : ",");
            str.append(sub.getGenres().get(i));
        }
        holder.genres.setText(str.toString());
        str.delete(0, str.length());
        for (int i = 0; i < sub.getDirectors().size(); i++) {
            str.append(i == 0 ? "" : "/").append(sub.getDirectors().get(i).getName());
        }
        holder.directors.setText(str.toString());
        str.delete(0, str.length());
        for (int i = 0; i < sub.getCasts().size(); i++) {
            str.append(i == 0 ? "" : "/").append(sub.getCasts().get(i).getName());
        }
        holder.casts.setText(str.toString());
        imageLoader.displayImage(sub.getImages().getLarge(),
                holder.image, options, imageLoadingListener);
    }

    private void showFootView(FootViewHolder viewHolder, int position) {
        if (position == 0) {
            viewHolder.itemView.setVisibility(View.GONE);
        } else if (loadCompleted()) {
            viewHolder.progressBar.setVisibility(View.GONE);
            viewHolder.tip.setText(mContext.getString(R.string.load_completed));
            viewHolder.itemView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tip.setText(mContext.getString(R.string.loading));
            viewHolder.itemView.setVisibility(View.VISIBLE);
        }
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

        @Bind(R.id.iv_coming_images)
        ImageView image;
        @Bind(R.id.ll_coming_rating)
        LinearLayout rating;
        @Bind(R.id.rb_coming_rating)
        RatingBar ratingBar;
        @Bind(R.id.tv_coming_rating)
        TextView text_rating;
        @Bind(R.id.tv_coming_collect_count)
        TextView collect_count;
        @Bind(R.id.tv_coming_title)
        TextView title;
        @Bind(R.id.tv_coming_original_title)
        TextView original_title;
        @Bind(R.id.tv_coming_genres)
        TextView genres;
        @Bind(R.id.tv_coming_director)
        TextView directors;
        @Bind(R.id.tv_coming_casts1)
        TextView casts;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCallback != null) {
                        int position = getLayoutPosition();
                        mCallback.onItemClick(mData.get(position).getId());
                    }
                }
            });
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
