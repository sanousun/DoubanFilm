package com.shenhui.doubanfilm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shenhui.doubanfilm.MyApplication;
import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.adapter.SimSubAdapter;
import com.shenhui.doubanfilm.base.BaseAdapter;
import com.shenhui.doubanfilm.base.BaseFragment;
import com.shenhui.doubanfilm.bean.SimpleSubjectBean;
import com.shenhui.doubanfilm.support.Constant;
import com.shenhui.doubanfilm.ui.activity.SubjectActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TopPagerFragment extends BaseFragment
        implements BaseAdapter.OnItemClickListener, View.OnClickListener {

    private static final int TOP250_COUNT = 25;
    private static final int TOP250_TOTAL = 50;
    private static final String JSON_SUBJECTS = "subjects";
    private static final String VOLLEY_TAG = "TOP_FRAGMENT";
    public static final String KEY_FRAGMENT_TOP = "top";

    private int mPosition;
    private int mStart;
    private boolean mFirstLoad = true;

    private SimSubAdapter mAdapter;
    private List<SimpleSubjectBean> mData = new ArrayList<>();

    public static TopPagerFragment newInstance(int top) {
        TopPagerFragment fragment = new TopPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_FRAGMENT_TOP, top);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mPosition = bundle.getInt(KEY_FRAGMENT_TOP);
        mStart = mPosition * TOP250_TOTAL;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {
        super.initData();
        mAdapter = new SimSubAdapter(getActivity(), mData);
        mRecView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mAdapter.setOnItemClickListener(this);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                volley_Get(String.format("%d", mStart));
            }
        });
        mFloatBtn.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFirstLoad) {
            List<SimpleSubjectBean> data;
            if ((data = MyApplication.getDataSource().
                    getTop(String.format("%d", mStart))) != null) {
                mData = data;
                mAdapter.updateList(mData, TOP250_TOTAL);
                setOnScrollListener();
            } else {
                volley_Get(mStart + "");
            }
            mFirstLoad = false;
        }
    }

    private void volley_Get(final String start) {
        String url = Constant.API +
                Constant.TOP250 + "?start=" + start + "&count=" + TOP250_COUNT;
        mRefreshLayout.setRefreshing(true);
        JsonObjectRequest request = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String content = response.getString(JSON_SUBJECTS);
                            mData = MyApplication.
                                    getDataSource().insertOrUpDate(start, content);
                            mAdapter.updateList(mData, TOP250_TOTAL);
                            setOnScrollListener();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            if (mRefreshLayout.isRefreshing())
                                mRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                        if (mRefreshLayout.isRefreshing())
                            mRefreshLayout.setRefreshing(false);
                    }
                });
        request.setTag(VOLLEY_TAG + mPosition);
        MyApplication.getHttpQueue().add(request);
    }

    /**
     * 为RecyclerView设置下拉刷新及floatingActionButton的消失出现
     */
    private void setOnScrollListener() {
        mRecView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            boolean isShow = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 3 > mAdapter.getItemCount()) {
                    if (mAdapter.getItemCount() - 1 < mAdapter.getTotal()) {
                        loadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager manager = (LinearLayoutManager) mRecView.getLayoutManager();
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = manager.findLastVisibleItemPosition();
                if (manager.findFirstVisibleItemPosition() == 0) {
                    if (isShow) {
                        animForGone();
                        isShow = false;
                    }
                } else if (dy < -50 && !isShow) {
                    animForVisible();
                    isShow = true;
                } else if (dy > 20 && isShow) {
                    animForGone();
                    isShow = false;
                }
            }
        });
    }

    private void loadMore() {
        int moreStart = mStart + TOP250_COUNT;
        List<SimpleSubjectBean> data;
        if ((data = MyApplication.getDataSource().getTop(moreStart + "")) != null) {
            mAdapter.loadMoreData(data);
        } else {
            volley_Get_More(moreStart + "");
        }
    }

    private void volley_Get_More(final String start) {
        String url = Constant.API + Constant.TOP250 +
                "?start=" + start + "&count=" + TOP250_COUNT;
        JsonObjectRequest request = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String content = response.getString(JSON_SUBJECTS);
                            mData = MyApplication.
                                    getDataSource().insertOrUpDate(start, content);
                            mAdapter.loadMoreData(mData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        request.setTag(VOLLEY_TAG + mPosition);
        MyApplication.getHttpQueue().add(request);
    }

    @Override
    public void onStop() {
        super.onStop();
        MyApplication.getHttpQueue().cancelAll(VOLLEY_TAG + mPosition);
    }

    @Override
    public void onItemClick(String id) {
        SubjectActivity.toActivity(getActivity(), id);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fragment:
                mRecView.smoothScrollToPosition(0);
        }
    }

    private void animForGone() {
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_gone);
        mFloatBtn.setAnimation(anim);
        mFloatBtn.setVisibility(View.GONE);
    }

    private void animForVisible() {
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_visible);
        mFloatBtn.setAnimation(anim);
        mFloatBtn.setVisibility(View.VISIBLE);
    }
}
