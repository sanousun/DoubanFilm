package com.shenhui.doubanfilm.base;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created with Android Studio.
 * <p>
 * User: dashu
 * date: 2017/11/18
 * time: 下午6:53
 * desc: 适用于 dataBinding 框架的 RecyclerView.ViewHolder，提供了一个通用的 mViewDataBinding 用于数据绑定
 */

public class BaseBindingViewHolder extends RecyclerView.ViewHolder {

    private ViewDataBinding mViewDataBinding;

    public BaseBindingViewHolder(ViewDataBinding viewDataBinding) {
        super(viewDataBinding.getRoot());
        mViewDataBinding = viewDataBinding;
    }

    public ViewDataBinding getViewDataBinding() {
        return mViewDataBinding;
    }
}
