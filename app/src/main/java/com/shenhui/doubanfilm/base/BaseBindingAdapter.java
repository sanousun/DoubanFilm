package com.shenhui.doubanfilm.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
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

    public abstract @LayoutRes int getItemLayoutRes(int viewType);

    @Override
    public BaseBindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseBindingViewHolder(parent, getItemLayoutRes(viewType));
    }

    @Override
    public void onBindViewHolder(BaseBindingViewHolder holder, int position) {
        holder.getViewDataBinding().setVariable(BR.item, getData(position));
    }
}
