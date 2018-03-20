package com.shenhui.doubanfilm.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.shenhui.doubanfilm.BR;

import java.util.List;

/**
 * Created with Android Studio.
 * <p>
 * User: dashu
 * date: 2017/11/18
 * time: 下午6:50
 * desc: 适用于 dataBinding 框架的 RecyclerView.Adapter
 */

public abstract class BaseBindingAdapter<T>
        extends BaseAdapter<T, BaseBindingViewHolder> {

    public BaseBindingAdapter(Context context) {
        super(context);
    }

    public BaseBindingAdapter(Context context, List<T> data) {
        super(context, data);
    }

    @LayoutRes
    public abstract int getItemLayoutRes(int viewType);

    @NonNull
    @Override
    public BaseBindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getItemLayoutRes(viewType), parent, false);
        return new BaseBindingViewHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseBindingViewHolder holder, int position) {
        holder.getViewDataBinding().setVariable(BR.item, getData(position));
    }
}
