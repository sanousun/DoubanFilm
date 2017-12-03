package com.shenhui.doubanfilm.adapter;

import android.content.Context;

import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.base.BaseBindingAdapter;
import com.shenhui.doubanfilm.data.bean.SimpleMovieInfo;

/**
 * Created with Android Studio.
 * <p>
 * author: sanousun
 * date: 2017/11/21
 * time: 下午3:26
 * desc: 电影列表
 */

public class SimpleMovieAdapter extends BaseBindingAdapter<SimpleMovieInfo> {
    public SimpleMovieAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemLayoutRes(int viewType) {
        return R.layout.item_simple_movie;
    }
}
