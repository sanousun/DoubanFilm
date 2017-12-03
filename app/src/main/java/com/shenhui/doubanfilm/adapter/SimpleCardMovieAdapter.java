package com.shenhui.doubanfilm.adapter;

import android.content.Context;

import com.shenhui.doubanfilm.base.BaseBindingAdapter;

/**
 * Created with Android Studio.
 * <p>
 * author: sanousun
 * date: 2017/11/22
 * time: 下午11:19
 * desc: 简单电影卡片列表
 */

public class SimpleCardMovieAdapter extends BaseBindingAdapter {

    public SimpleCardMovieAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemLayoutRes(int viewType) {
        return 0;
    }
}
