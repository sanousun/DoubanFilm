package com.shenhui.doubanfilm.adapter;

import android.content.Context;

import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.base.BasePullAdapter;
import com.shenhui.doubanfilm.data.bean.SimpleMovieInfo;

/**
 * Created with Android Studio.
 * <p>
 * author: sanousun
 * date: 2017/11/21
 * time: 下午3:26
 * desc: 电影列表
 */

public class SimpleMovieAdapter extends BasePullAdapter<SimpleMovieInfo> {
    public SimpleMovieAdapter(Context context) {
        super(context);
    }

    @Override
    public int getNormalItemViewType(int position) {
        return 0;
    }

    @Override
    public int getNormalItemLayoutRes(int viewType) {
        return R.layout.item_simple_movie;
    }
}
