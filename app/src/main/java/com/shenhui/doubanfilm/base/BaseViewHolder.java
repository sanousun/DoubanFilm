package com.shenhui.doubanfilm.base;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created with Android Studio.
 * <p>
 * author: sanousun
 * date: 2017/11/20
 * time: 下午7:18
 * desc: ViewHolder 基类，简化了部分方法
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(ViewGroup parent, @LayoutRes int resId) {
        super(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));
    }
}
