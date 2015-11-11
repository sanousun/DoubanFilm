package com.shenhui.doubanfilm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.base.BaseAdapter;
import com.shenhui.doubanfilm.bean.SimpleSubjectBean;
import com.shenhui.doubanfilm.bean.BoxSubjectBean;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BoxAdapter extends BaseAdapter<BoxAdapter.ViewHolder> {

    private static final int FIRST = 1;
    private static final int SECOND = 2;
    private static final int THIRD = 3;

    private static final String GOLD = "#D9D919";
    private static final String SILVERY = "#D9D919";
    private static final String COPPER = "#B87333";

    private LayoutInflater mInflater;
    private List<BoxSubjectBean> mData;

    private ImageLoadingListener imageLoadingListener =
            new AnimateFirstDisplayListener();

    public static final String[] RANK = {"th", "1st", "2nd", "3rd"};

    public BoxAdapter(Context context, List<BoxSubjectBean> data) {
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_box_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        BoxSubjectBean subject = mData.get(position);
        int rank = subject.getRank();
        setRankTextColor(holder, rank);
        if (rank < 4) {
            holder.text_rank.setText(RANK[rank]);
        } else {
            holder.text_rank.setText(String.format("%d%s", rank, RANK[0]));
        }
        holder.image_isNew.setVisibility(
                subject.getNewX() ? View.VISIBLE : View.GONE);

        SimpleSubjectBean simSubject = subject.getSubject();
        float rating = (float) simSubject.getRating().getAverage();
        if (rating == 0) {
            holder.text_no_rating.setVisibility(View.VISIBLE);
            holder.ratingLayout.setVisibility(View.GONE);
        } else {
            holder.ratingBar.setRating(rating / 2);
            holder.text_rating.setText(String.format("%s", rating));
        }
        holder.text_title.setText(simSubject.getTitle());

        imageLoader.displayImage(simSubject.getImages().getLarge(),
                holder.image_film, options, imageLoadingListener);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setRankTextColor(ViewHolder holder, int rank) {
        switch (rank) {
            case FIRST:
                holder.text_rank.setTextColor(
                        Color.parseColor(GOLD));
                break;
            case SECOND:
                holder.text_rank.setTextColor(
                        Color.parseColor(SILVERY));
                break;
            case THIRD:
                holder.text_rank.setTextColor(
                        Color.parseColor(COPPER));
                break;
            default:
                holder.text_rank.setTextColor(
                        Color.GRAY);
                break;
        }
    }

    public void updateList(List<BoxSubjectBean> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_box_rank)
        TextView text_rank;
        @Bind(R.id.iv_box_isNew)
        ImageView image_isNew;
        @Bind(R.id.iv_box_image)
        ImageView image_film;
        @Bind(R.id.tv_box_title)
        TextView text_title;
        @Bind(R.id.tv_box_noRating)
        TextView text_no_rating;
        @Bind(R.id.rb_box_rating)
        RatingBar ratingBar;
        @Bind(R.id.tv_box_rating)
        TextView text_rating;
        @Bind(R.id.ll_box_rating)
        LinearLayout ratingLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCallback != null) {
                        int position = getLayoutPosition();
                        mCallback.onItemClick(
                                mData.get(position).getSubject().getId());
                    }
                }
            });
        }
    }

    private static class AnimateFirstDisplayListener
            extends SimpleImageLoadingListener {
        static final List<String> displayedImages =
                Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(
                String imageUri, View view, Bitmap loadedImage) {
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
