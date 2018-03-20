package com.shenhui.doubanfilm.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.databinding.ItemPullExtraBinding;

import java.util.List;

/**
 * @author dashu
 * @date 2018/3/19
 * desc: 带加载更多的Adapter抽象基类，集成了额外item
 */

public abstract class BasePullAdapter<T>
        extends BaseBindingAdapter<T>
        implements BasePullClickListener {

    private static final int TYPE_PULL = -1000;

    private BasePullInfo mBasePullInfo = new BasePullInfo();
    private boolean mIsLoading = false;
    private OnLoadListener mOnLoadListener;

    public void setOnPullListener(OnLoadListener onLoadListener) {
        mOnLoadListener = onLoadListener;
    }

    public BasePullAdapter(Context context) {
        super(context);
    }

    public BasePullAdapter(Context context, List<T> data) {
        super(context, data);
    }

    @Override
    public int getItemCount() {
        int dataCount = super.getItemCount();
        if (dataCount == 0) {
            return 0;
        }
        return dataCount + 1;
    }

    /**
     * 实际的数量
     *
     * @return 实际的数据量
     */
    public int getDataCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= super.getItemCount()) {
            return TYPE_PULL;
        } else {
            return getNormalItemViewType(position);
        }
    }

    /**
     * 根据位置获取视图类型
     *
     * @param position 位置
     * @return 视图类型
     */
    public abstract int getNormalItemViewType(int position);

    @Override
    public int getItemLayoutRes(int viewType) {
        if (viewType == TYPE_PULL) {
            return R.layout.item_pull_extra;
        }
        return getNormalItemLayoutRes(viewType);
    }

    /**
     * 根据视图类型获取布局文件
     *
     * @param viewType 除了-1000的viewType
     * @return 目标布局文件
     */
    @LayoutRes
    public abstract int getNormalItemLayoutRes(int viewType);

    @Override
    public void onBindViewHolder(@NonNull BaseBindingViewHolder holder, int position) {
        if (holder.getViewDataBinding() instanceof ItemPullExtraBinding) {
            ItemPullExtraBinding itemPullExtraBinding = (ItemPullExtraBinding) holder.getViewDataBinding();
            itemPullExtraBinding.setPullInfo(mBasePullInfo);
            itemPullExtraBinding.setPullListener(this);
            boolean canLoading = !mIsLoading && mBasePullInfo.isLoading() && mOnLoadListener != null;
            if (canLoading) {
                mIsLoading = true;
                mOnLoadListener.onLoad();
            }
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    public void showLoading() {
        showLoading(getContext().getString(R.string.item_pull_loading));
    }

    public void showLoading(String desc) {
        mIsLoading = false;
        mBasePullInfo.setStatus(BasePullInfo.STATUS_LOADING, desc);
        notifyItemChanged(getItemCount() - 1);
    }

    public void showError() {
        showError(getContext().getString(R.string.item_pull_error));
    }

    public void showError(String desc) {
        mIsLoading = false;
        mBasePullInfo.setStatus(BasePullInfo.STATUS_ERROR, desc);
        notifyItemChanged(getItemCount() - 1);
    }

    public void showNoMore() {
        showNoMore(getContext().getString(R.string.item_pull_no_more));
    }

    public void showNoMore(String desc) {
        mIsLoading = false;
        mBasePullInfo.setStatus(BasePullInfo.STATUS_NO_MORE, desc);
        notifyItemChanged(getItemCount() - 1);
    }

    @Override
    public void onClick() {
        showLoading();
    }

    public interface OnLoadListener {

        /**
         * 加载触发
         */
        void onLoad();
    }
}
