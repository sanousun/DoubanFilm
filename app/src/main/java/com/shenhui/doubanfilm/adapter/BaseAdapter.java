package com.shenhui.doubanfilm.adapter;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.app.MyApplication;
import com.shenhui.doubanfilm.support.AnimatorListenerAdapter;

public class BaseAdapter<T extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<T> {
    private int mLastPosition = -1;

    protected OnItemClickListener mCallback;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    protected DisplayImageOptions options = MyApplication.getLoaderOptions();

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
        final Context mContext = view.getContext();
        if (pos > mLastPosition) {
            view.setAlpha(0.0f);
            view.post(new Runnable() {
                @Override
                public void run() {
                    Animator animator = AnimatorInflater.loadAnimator(
                            mContext, R.animator.slide_from_right);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            view.setAlpha(1.0f);
                        }
                    });
                    animator.setTarget(view);
                    animator.start();
                }
            });
            mLastPosition = pos;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String id, String imageUrl);
    }
}
