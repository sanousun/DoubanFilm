package com.shenhui.doubanfilm.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.base.BaseBindingAdapter;
import com.shenhui.doubanfilm.base.BaseBindingViewHolder;
import com.shenhui.doubanfilm.data.bean.SimpleBoxMovieInfo;
import com.shenhui.doubanfilm.databinding.ItemSimpleBoxMovieBinding;

/**
 * Created with Android Studio.
 * <p>
 * author: sanousun
 * date: 2017/11/21
 * time: 下午3:28
 * desc: 票房电影列表
 */

public class SimpleBoxMovieAdapter extends BaseBindingAdapter<SimpleBoxMovieInfo> {
    public SimpleBoxMovieAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(BaseBindingViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemSimpleBoxMovieBinding boxMovieBinding =
                (ItemSimpleBoxMovieBinding) holder.getViewDataBinding();
        ViewGroup.LayoutParams layoutParams =
                boxMovieBinding.ivSubject.getLayoutParams();
        layoutParams.height = (int) (layoutParams.width * 1.42);
    }

    @Override
    public int getItemLayoutRes(int viewType) {
        return R.layout.item_simple_box_movie;
    }
}
