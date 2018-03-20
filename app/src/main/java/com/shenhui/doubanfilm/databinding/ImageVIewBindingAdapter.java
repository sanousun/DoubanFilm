package com.shenhui.doubanfilm.databinding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.shenhui.doubanfilm.app.GlideApp;

/**
 * Created with Android Studio.
 * <p>
 * author: sanousun
 * date: 2017/12/3
 * time: 下午3:26
 * desc: ImageView的属性绑定自动义
 */

public class ImageVIewBindingAdapter {

    @BindingAdapter("android:src")
    public static void setImageUrl(ImageView view, String url) {
        GlideApp.with(view.getContext())
                .load(url)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(view);
    }
}
