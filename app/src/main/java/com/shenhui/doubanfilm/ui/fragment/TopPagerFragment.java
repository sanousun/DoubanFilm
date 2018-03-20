package com.shenhui.doubanfilm.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shenhui.doubanfilm.app.MyApplication;
import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.adapter.SimpleSubjectAdapter;
import com.shenhui.doubanfilm.adapter.BaseAdapter;
import com.shenhui.doubanfilm.bean.SimpleSubjectBean;
import com.shenhui.doubanfilm.support.AnimatorListenerAdapter;
import com.shenhui.doubanfilm.support.Constant;
import com.shenhui.doubanfilm.support.util.DensityUtil;
import com.shenhui.doubanfilm.ui.BaseFragment;
import com.shenhui.doubanfilm.ui.activity.SubjectActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

public class TopPagerFragment extends BaseFragment
        implements BaseAdapter.OnItemClickListener, View.OnClickListener {

    private static final int TOP250_COUNT = 25;
    private static final int TOP250_TOTAL = 50;
    private static final String JSON_SUBJECTS = "subjects";
    private static final String VOLLEY_TAG = "top_fragment";
    public static final String KEY_FRAGMENT_TOP = "key_top";

    private int mPosition;
    private String mStart;
    private boolean isFirstLoad = true;
    private boolean isRefresh = false;

    private SimpleSubjectAdapter mAdapter;
    private List<SimpleSubjectBean> mData = new ArrayList<>();

    /**
     * 为RecyclerView设置下拉刷新及floatingActionButton的消失出现
     */
    private OnScrollListener mScrollListener;

    //-------------------------------------------------------------------

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
        mStart = String.valueOf(mPosition * TOP250_TOTAL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {
        mAdapter = new SimpleSubjectAdapter(getActivity(), mData);
        mRecView.setAdapter(mAdapter);
        int padding = -DensityUtil.dp2px(getContext(), 2f);
        mRecView.setPadding(padding, padding, padding, padding);
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnItemClickListener(this);
        mRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        isRefresh = true;
                        volley_Get(mStart);
                    }
                });
        mFloatBtn.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            new MyAsyncTask().execute(mStart);
            isFirstLoad = false;
        }
    }

    private void volley_Get(final String start) {
        String url = String.format("%s%s?start=%s&count=%d",
                Constant.API, Constant.TOP250, start, TOP250_COUNT);
        mRefreshLayout.setRefreshing(true);
        JsonObjectRequest request = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String content = response.getString(JSON_SUBJECTS);
                            mData = MyApplication.
                                    getDataSource().insertOrUpDateTop(start, content);
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
        MyApplication.addRequest(request,VOLLEY_TAG + mPosition);
    }

    private void loadMore() {
        String moreStart = String.valueOf(mPosition * TOP250_TOTAL + TOP250_COUNT);
        List<SimpleSubjectBean> data;
        if ((data = MyApplication.getDataSource().
                getTop(moreStart)) != null && !isRefresh) {
            mAdapter.loadMoreData(data);
        } else {
            volley_Get_More(moreStart);
        }
    }

    private void volley_Get_More(final String start) {
        String url = String.format("%s%s?start=%s&count=%d",
                Constant.API, Constant.TOP250, start, TOP250_COUNT);
        JsonObjectRequest request = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String content = response.getString(JSON_SUBJECTS);
                            List<SimpleSubjectBean> data = MyApplication.
                                    getDataSource().insertOrUpDateTop(start, content);
                            mAdapter.loadMoreData(data);
                            isRefresh = false;
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
        MyApplication.addRequest(request,VOLLEY_TAG + mPosition);
    }


    /**
     * 为RecyclerView设置下拉刷新及floatingActionButton的消失出现
     */
    private void setOnScrollListener() {
        if (mRecView == null) {
            return;
        }
        if (mScrollListener == null) {
            mScrollListener = new OnScrollListener() {
                int lastVisibleItem;
                boolean isShow = false;

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView,
                                                 int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == SCROLL_STATE_IDLE
                            && lastVisibleItem + 2 > mAdapter.getItemCount()) {
                        if (mAdapter.getItemCount() - 1 < mAdapter.getTotalDataCount()) {
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
                            animatorForGone();
                            isShow = false;
                        }
                    } else if (dy < -50 && !isShow) {
                        animatorForVisible();
                        isShow = true;
                    } else if (dy > 20 && isShow) {
                        animatorForGone();
                        isShow = false;
                    }
                }
            };
            mRecView.addOnScrollListener(mScrollListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MyApplication.removeRequest(VOLLEY_TAG + mPosition);
    }

    @Override
    public void onItemClick(String id, String imageUrl) {
        SubjectActivity.toActivity(getActivity(), id, imageUrl);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fragment:
                mRecView.smoothScrollToPosition(0);
        }
    }

    private void animatorForGone() {
        Animator animator = AnimatorInflater.loadAnimator(getActivity(), R.animator.scale_gone);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                mFloatBtn.setVisibility(View.GONE);
            }
        });
        animator.setTarget(mFloatBtn);
        animator.start();
    }

    private void animatorForVisible() {
        Animator animator = AnimatorInflater.loadAnimator(getActivity(), R.animator.scale_visible);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                mFloatBtn.setVisibility(View.VISIBLE);
            }
        });
        animator.setTarget(mFloatBtn);
        animator.start();
    }

    private class MyAsyncTask extends AsyncTask<String, Void, List<SimpleSubjectBean>> {

        @Override
        protected List<SimpleSubjectBean> doInBackground(String... strings) {
            String start = strings[0];
            return MyApplication.getDataSource().getTop(start);
        }

        @Override
        protected void onPreExecute() {
            mRefreshLayout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(List<SimpleSubjectBean> data) {
            if (data != null) {
                mData = data;
                mAdapter.updateList(mData, TOP250_TOTAL);
                mRefreshLayout.setRefreshing(false);
                setOnScrollListener();
            } else {
                volley_Get(mStart);
            }
        }
    }
}
