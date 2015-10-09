package com.shenhui.doubanfilm.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shenhui.doubanfilm.R;

/**
 * Created by sanousun on 2015/8/31.
 */
public class BaseFragment extends Fragment {

    protected int layoutId = R.layout.fragment_base;

    protected RecyclerView mRecView;
    protected SwipeRefreshLayout mRefreshLayout;
    protected FloatingActionButton mFloatBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId, container, false);
        mRecView = (RecyclerView) view.findViewById(R.id.rv_fragment);
        mRecView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fresh_fragment);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setProgressViewOffset(false, 0, 100);
        mFloatBtn = (FloatingActionButton) view.findViewById(R.id.btn_fragment);
        initData();
        initEvent();
        return view;
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 初始化事件
     */
    protected void initEvent() {

    }
}
