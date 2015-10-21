package com.shenhui.doubanfilm.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.shenhui.doubanfilm.R;

public class BaseAdapter<T extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<T> {
    private int mLastPosition = -1;

    protected OnItemClickListener mCallback;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    protected DisplayImageOptions options = new DisplayImageOptions.Builder().
            showImageOnLoading(R.drawable.noimage).
            showImageOnFail(R.drawable.noimage).
            showImageForEmptyUri(R.drawable.lks_for_blank_url).
            imageScaleType(ImageScaleType.EXACTLY_STRETCHED).
            cacheInMemory(true).
            cacheOnDisk(true).
            considerExifParams(true).
            build();

    public void setOnItemClickListener(OnItemClickListener listener) {
        mCallback = listener;
    }

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

    public void showItemAnim(final View view, int pos) {
        final Context context = view.getContext();
        if (pos > mLastPosition) {
            view.setAlpha(0);
            view.post(new Runnable() {
                @Override
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            view.setAlpha(1);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    view.setAnimation(animation);
                }
            });
            mLastPosition = pos;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String id);
    }
}
