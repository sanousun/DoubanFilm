package com.shenhui.doubanfilm.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.shenhui.doubanfilm.MyApplication;
import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.adapter.SimSubAdapter;
import com.shenhui.doubanfilm.base.BaseFragment;
import com.shenhui.doubanfilm.bean.SimpleSub;
import com.shenhui.doubanfilm.support.Constant;
import com.shenhui.doubanfilm.ui.activity.SubjectActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanousun on 2015/9/27.
 */
public class TopPagerFragment extends BaseFragment implements SimSubAdapter.OnItemClickListener {

    private static final int TOP250_COUNT = 25;
    private static final int TOP250_TOTAL = 50;
    private static final String JSON_SUBJECTS = "subjects";
    private static final String VOLLEY_TAG = "TOP_FRAGMENT";

    private int mPosition;
    private int mStart;
    private boolean mFirstLoad = true;

    private SimSubAdapter mAdapter;
    private List<SimpleSub> mData = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mPosition = bundle.getInt(TopFragment.TOP_FRAGMENT_TOP);
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
                volley_Get(mStart + "");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFirstLoad) {
            List<SimpleSub> data;
            if ((data = MyApplication.getDataSource().getTop(mStart + "")) != null) {
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
        String url = Constant.API + Constant.TOP250 + "?start=" + start + "&count=" + TOP250_COUNT;
        mRefreshLayout.setRefreshing(true);
        JsonObjectRequest request = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String content = response.getString(JSON_SUBJECTS);
                            mData = MyApplication.
                                    getDataSource().insertOrUpDate(start, content);
                            Log.i("xyz", "adapter");
                            mAdapter.updateList(mData, TOP250_TOTAL);
                            Log.i("xyz", "scroll");
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
                        Log.i("xyz", error.toString());
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
        Log.i("xyz", "setOnScrollListener()");
        mRecView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

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
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem =
                        ((LinearLayoutManager) mRecView.getLayoutManager()).
                                findLastVisibleItemPosition();
                Log.i("xyz", "lastVisibleItem-->" + lastVisibleItem);
            }
        });
    }

    private void loadMore() {
        Log.i("xyz", "loadMore()");
        int moreStart = mStart + TOP250_COUNT;
        List<SimpleSub> data;
        if ((data = MyApplication.getDataSource().getTop(moreStart + "")) != null) {
            mAdapter.loadMoreData(data);
        } else {
            volley_Get_More(moreStart + "");
        }
    }

    private void volley_Get_More(final String start) {
        String url = Constant.API + Constant.TOP250 + "?start=" + start + "&count=" + TOP250_COUNT;
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
    public void itemClick(String id) {
        Intent intent = new Intent(getActivity(), SubjectActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
