package com.shenhui.doubanfilm.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with Android Studio.
 * <p>
 * author: dashu
 * date: 2017/11/20
 * time: 下午7:18
 * desc: Adapter的抽象基类，集成了一些数据操作方法
 */

public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private Context mContext;
    private List<T> mData;

    public BaseAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
    }

    public BaseAdapter(Context context, List<T> data) {
        mContext = context;
        mData = data;
        if (mData == null) {
            mData = new ArrayList<>();
        }
    }

    public T getData(int pos) {
        return mData.get(pos);
    }

    /**
     * 单个末尾插入
     *
     * @param t 插入的数据
     */
    public void insert(T t) {
        mData.add(t);
        notifyItemInserted(mData.size() - 1);
    }

    /**
     * 单个任意位置插入
     *
     * @param position 插入的位置 0~mData.size()
     * @param t        插入的数据
     */
    public void insert(int position, T t) {
        mData.add(position, t);
        notifyItemInserted(position);
    }

    /**
     * 集合末尾插入
     *
     * @param ts 插入的数据集合
     */
    public void rangeInsert(Collection<T> ts) {
        mData.addAll(ts);
        notifyItemRangeInserted(mData.size() - ts.size(), ts.size());
    }

    /**
     * 集合任意位置插入
     *
     * @param positionStart 插入的起始位置 0~mData.size()
     * @param ts            插入的数据集合
     */
    public void rangeInsert(int positionStart, Collection<T> ts) {
        mData.addAll(positionStart, ts);
        notifyItemRangeInserted(positionStart, ts.size());
    }

    /**
     * 移除任意位置的单个数据
     *
     * @param position 移除数据的位置 0~mData.size()-1
     */
    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 移除任意位置的批量数据
     *
     * @param positionStart 移除数据的起始位置
     * @param itemCount     移除数据的大小
     */
    public void rangeRemove(int positionStart, int itemCount) {
        mData.removeAll(mData.subList(positionStart, positionStart + itemCount));
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    /**
     * 改变对应位置上的单个数据
     *
     * @param position 改变数据的位置
     * @param t        改变后的数据
     */
    public void change(int position, T t) {
        mData.remove(position);
        mData.add(position, t);
        notifyItemChanged(position);
    }

    /**
     * 改变对应位置上的批量数据
     *
     * @param positionStart 改变批量数据的起始位置
     * @param ts            改变后的数据集合
     */
    public void rangeChange(int positionStart, Collection<T> ts) {
        mData.removeAll(mData.subList(positionStart, positionStart + ts.size()));
        mData.addAll(positionStart, ts);
        notifyItemRangeChanged(positionStart, ts.size());
    }

    /**
     * 批量改变数据，一般指的是直接对数组操作后更新视图
     *
     * @param positionStart 数据改变的起始位置
     * @param itemCount     数据改变的数量
     */
    public void rangeChange(int positionStart, int itemCount) {
        notifyItemRangeChanged(positionStart, itemCount);
    }

    /**
     * 批量改变数据但是只是改变一部分视图，一般指的是直接对数组操作后更新视图
     *
     * @param positionStart 数据改变的起始位置
     * @param itemCount     数据改变的数量
     * @param payload       局部更改的视图
     */
    public void rangeChange(int positionStart, int itemCount, List<Object> payload) {
        notifyItemRangeChanged(positionStart, itemCount, payload);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
