package com.shenhui.doubanfilm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.base.BaseAnimAdapter;
import com.shenhui.doubanfilm.bean.Subject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CollectAdapter extends BaseAnimAdapter<CollectAdapter.ViewHolder> {

    private static final String URI_FOR_FILE =
            "file://storage/emulated/0/Android/data/com.shenhui.doubanfillm/files/Pictures/";
    private static final String URI_FOR_IMAGE = ".png";

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Subject> mData;

    private OnItemClickListener callback;

    private Subject undoSub;

    private ImageLoadingListener imageLoadingListener = new AnimateFirstDisplayListener();

    public CollectAdapter(Context context, List<Subject> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void updateList(List<Subject> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener callback) {
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_collect_layout, parent, false);
        return new ViewHolder(view, new OnResponseCLickListener() {
            @Override
            public void onWholeClick(int pos) {
                callback.itemClick(mData.get(pos).getId());
            }

            @Override
            public void onOverflowClick(View v, final int pos) {
                PopupMenu menu = new PopupMenu(mContext, v);
                MenuInflater inflater = menu.getMenuInflater();
                inflater.inflate(R.menu.item_overflow, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        removeItem(pos);
                        return true;
                    }
                });
                menu.show();
            }
        });
    }

    /**
     * 移除相应的item
     */
    private void removeItem(int pos) {
        notifyItemRemoved(pos);
        undoSub = mData.get(pos);
        mData.remove(pos);
        callback.itemRemove(pos, undoSub.getId());
    }

    /**
     * 用于撤销“取消收藏”操作
     */
    public void cancelRemove(int pos) {
        if (undoSub != null) {
            notifyItemInserted(pos);
            mData.add(pos, undoSub);
            undoSub = null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Subject sub = mData.get(position);
        float rate = (float) sub.getRating().getAverage();
        holder.ratingBar.setRating(rate / 2);
        holder.text_rating.setText(String.format("%s", rate));
        holder.collect_count.setText(String.format("%d", sub.getCollect_count()));
        String title = sub.getTitle();
        String original_title = sub.getOriginal_title();
        holder.title.setText(title);
        if (original_title.equals(title)) {
            holder.original_title.setVisibility(View.GONE);
        } else {
            holder.original_title.setText(original_title);
            holder.original_title.setVisibility(View.VISIBLE);
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < sub.getGenres().size(); i++) {
            s.append(i == 0 ? "" : ",").append(sub.getGenres().get(i));
        }
        holder.genres.setText(s.toString());
        s = new StringBuilder();
        for (int i = 0; i < sub.getDirectors().size(); i++) {
            s.append(i == 0 ? "" : "/").append(sub.getDirectors().get(i).getName());
        }
        holder.directors.setText(s.toString());
        s.delete(0, s.length());
        for (int i = 0; i < sub.getCasts().size(); i++) {
            s.append(i == 0 ? "" : "/");
            s.append(sub.getCasts().get(i).getName());
        }
        holder.casts.setText(s.toString());
        String uri = URI_FOR_FILE + sub.getId() + URI_FOR_IMAGE;
        imageLoader.displayImage(uri, holder.image, options, imageLoadingListener);
        showItemAnim(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.iv_collect_images)
        ImageView image;
        @Bind(R.id.rb_collect_rating)
        RatingBar ratingBar;
        @Bind(R.id.tv_collect_rating)
        TextView text_rating;
        @Bind(R.id.tv_collect_collect_count)
        TextView collect_count;
        @Bind(R.id.tv_collect_title)
        TextView title;
        @Bind(R.id.tv_collect_original_title)
        TextView original_title;
        @Bind(R.id.tv_collect_genres)
        TextView genres;
        @Bind(R.id.tv_collect_director)
        TextView directors;
        @Bind(R.id.tv_collect_casts)
        TextView casts;
        @Bind(R.id.iv_collect_more)
        ImageView over_flow;

        private OnResponseCLickListener mListener;

        public ViewHolder(View itemView, OnResponseCLickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mListener = listener;
            over_flow.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == over_flow) {
                mListener.onOverflowClick(view, getAdapterPosition());
            } else {
                mListener.onWholeClick(getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void itemClick(String id);

        void itemRemove(int pos, String id);
    }

    public interface OnResponseCLickListener {
        void onWholeClick(int pos);

        void onOverflowClick(View v, int pos);
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
