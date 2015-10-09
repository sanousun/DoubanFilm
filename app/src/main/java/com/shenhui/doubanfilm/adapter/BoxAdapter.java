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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.bean.SimpleSub;
import com.shenhui.doubanfilm.bean.USBoxSub;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sanousun on 2015/8/12.
 */
public class BoxAdapter extends RecyclerView.Adapter<BoxAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<USBoxSub> mData;

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

    public static final String[] RANK = {"1st", "2nd", "3rd", "4th", "5th",
            "6th", "7th", "8th", "9th", "10th", "11th", "12th", "13th", "14th", "15th"};

    private OnItemClickListener mListener;

    public BoxAdapter(Context context, List<USBoxSub> data) {
        this.mContext = context;
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_box_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        USBoxSub subject = mData.get(position);
        setRankText(holder, subject.getRank());
        holder.text_rank.setText(RANK[subject.getRank() - 1]);
        holder.image_isNew.setVisibility(subject.getNewX() ? View.VISIBLE : View.GONE);

        SimpleSub simSubject = subject.getSubject();
        float rating = (float) simSubject.getRating().getAverage();
        if (rating == 0) {
            holder.text_no_rating.setVisibility(View.VISIBLE);
            holder.ratingLayout.setVisibility(View.GONE);
        } else {
            holder.ratingBar.setRating(rating / 2);
            holder.text_rating.setText(rating + "");
        }
        holder.text_title.setText(simSubject.getTitle());

        imageLoader.displayImage(simSubject.getImages().getLarge(),
                holder.image_film, options, imageLoadingListener);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.itemClick(mData.get(position).getSubject().getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setRankText(ViewHolder holder, int rank) {
        switch (rank) {
            case 1:
                holder.text_rank.setTextColor(Color.parseColor("#D9D919"));
                break;
            case 2:
                holder.text_rank.setTextColor(Color.parseColor("#E5E5E5"));
                break;
            case 3:
                holder.text_rank.setTextColor(Color.parseColor("#B5A642"));
                break;
            default:
                holder.text_rank.setTextColor(Color.GRAY);
                break;
        }
    }

    public void updateList(List<USBoxSub> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text_rank;
        public ImageView image_isNew;
        public ImageView image_film;
        public TextView text_no_rating;
        public LinearLayout ratingLayout;
        public RatingBar ratingBar;
        public TextView text_rating;
        public TextView text_title;

        public ViewHolder(View itemView) {
            super(itemView);
            text_rank = (TextView) itemView.findViewById(R.id.tv_grid_rank);
            image_isNew = (ImageView) itemView.findViewById(R.id.iv_grid_isNew);
            image_film = (ImageView) itemView.findViewById(R.id.iv_grid_image);
            text_no_rating = (TextView) itemView.findViewById(R.id.tv_grid_noRating);
            ratingLayout = (LinearLayout) itemView.findViewById(R.id.ll_grid_rating);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rb_grid_rating);
            text_rating = (TextView) itemView.findViewById(R.id.tv_grid_rating);
            text_title = (TextView) itemView.findViewById(R.id.tv_grid_title);
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

    public interface OnItemClickListener {
        public void itemClick(String id);
    }
}
