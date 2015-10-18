package com.shenhui.doubanfilm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shenhui.doubanfilm.MyApplication;
import com.shenhui.doubanfilm.adapter.CollectAdapter;
import com.shenhui.doubanfilm.base.BaseFragment;
import com.shenhui.doubanfilm.bean.Subject;
import com.shenhui.doubanfilm.ui.activity.SubjectActivity;

import java.util.ArrayList;
import java.util.List;

public class CollectFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener,
        CollectAdapter.OnItemClickListener {

    private List<Subject> mData = new ArrayList<>();
    private CollectAdapter mAdapter;
    private View mView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, container, savedInstanceState);
        return mView;
    }

    @Override
    protected void initData() {
        mAdapter = new CollectAdapter(getActivity(), mData);
        mRecView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mRefreshLayout.setRefreshing(true);
        update();
    }

    @Override
    protected void initEvent() {
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        update();
    }

    private void update() {
        mData = MyApplication.getDataSource().filmOfAll();
        mAdapter.updateList(mData);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void itemClick(String id) {
        SubjectActivity.toActivity(getActivity(), id);
    }

    @Override
    public void itemRemove(final int pos, final String id) {
        Snackbar.make(mView, "是否要取消收藏...", Snackbar.LENGTH_LONG).
                setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyApplication.getDataSource().deleteFilm(id);
                    }
                }).
                setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                            mAdapter.cancelRemove(pos);
                        }
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {
                        super.onShown(snackbar);
                    }
                }).show();
    }
}
