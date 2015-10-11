package com.shenhui.doubanfilm.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shenhui.doubanfilm.MyApplication;
import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.adapter.BoxAdapter;
import com.shenhui.doubanfilm.adapter.SimSubAdapter;
import com.shenhui.doubanfilm.bean.SimpleSub;
import com.shenhui.doubanfilm.bean.USBoxSub;
import com.shenhui.doubanfilm.support.Constant;
import com.shenhui.doubanfilm.ui.activity.SubjectActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * refresh的显示，先设置一个空的adapter数据，再通过网络得到数据并更新
 * Created by sanousun on 2015/9/1.
 */
public class HomePagerFragment extends Fragment {

    private static final String AUTO_REFRESH = "auto refresh?";

    private static final String JSON_TOTAL = "total";
    private static final String JSON_SUBJECTS = "subjects";

    @Bind(R.id.rv_fragment)
    RecyclerView mRecView;
    @Bind(R.id.fresh_fragment)
    SwipeRefreshLayout mRefresh;
    @Bind(R.id.btn_fragment)
    FloatingActionButton mBtn;

    private SimSubAdapter mSimAdapter;
    private BoxAdapter mBoxAdapter;
    private List<SimpleSub> mSimData = new ArrayList<>();
    private List<USBoxSub> mBoxData = new ArrayList<>();

    private String mTitle = "null";
    private String requestUrl;
    private int totalItem;
    private boolean isFirstRefresh = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitle = bundle.getString(HomeFragment.HOME_FRAGMENT_TITLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        ButterKnife.bind(this, view);
        mRefresh.setColorSchemeResources(R.color.colorPrimary);
        mRefresh.setProgressViewOffset(false, 0, 100);
        initData();
        initEvent();
        return view;
    }

    private void initEvent() {
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
            }
        });
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRecView.getAdapter() != null) {
                    mRecView.smoothScrollToPosition(0);
                }
            }
        });
    }

    private void initData() {
        switch (mTitle) {
            case "正在热映":
                initSimpleRecyclerView(false);
                break;
            case "即将上映":
                initSimpleRecyclerView(true);
                break;
            case "北美票房":
                initBoxRecyclerView();
                break;
        }
    }

    /**
     * 使用可以保存图片的SubjectActivity
     */
    private void initSimpleRecyclerView(boolean isComing) {
        LinearLayoutManager inManager = new LinearLayoutManager(getActivity());
        inManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecView.setLayoutManager(inManager);
        mSimAdapter = new SimSubAdapter(getActivity(), mSimData, isComing);
        mSimAdapter.setOnItemClickListener(new SimSubAdapter.OnItemClickListener() {
            @Override
            public void itemClick(String id) {
                prepareIntent(SubjectActivity.class, id);
            }
        });
        mRecView.setAdapter(mSimAdapter);
    }

    private void initBoxRecyclerView() {
        GridLayoutManager boxManager = new GridLayoutManager(getActivity(), 3);
        mRecView.setLayoutManager(boxManager);
        mBoxAdapter = new BoxAdapter(getActivity(), mBoxData);
        mBoxAdapter.setOnItemClickListener(new BoxAdapter.OnItemClickListener() {
            @Override
            public void itemClick(String id) {
                prepareIntent(SubjectActivity.class, id);
            }
        });
        mRecView.setAdapter(mBoxAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (isFirstRefresh || sharedPreferences.getBoolean(AUTO_REFRESH, true)) {
            updateData();
            isFirstRefresh = false;
        }
    }

    private void updateData() {
        switch (mTitle) {
            case "正在热映":
                requestUrl = Constant.API + Constant.IN_THEATERS;
                volley_Get_Coming();
                break;
            case "即将上映":
                requestUrl = Constant.API + Constant.COMING;
                volley_Get_Coming();
                break;
            case "北美票房":
                requestUrl = Constant.API + Constant.US_BOX;
                volley_Get_USBox();
                break;
        }
    }

    /**
     * 通过Volley框架的全局消息队列获取到url对应的数据
     */
    private void volley_Get_Coming() {
        mRefresh.setRefreshing(true);
        JsonObjectRequest request = new JsonObjectRequest(requestUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            totalItem = response.getInt(JSON_TOTAL);
                            mSimData = new Gson().fromJson(response.getString(JSON_SUBJECTS),
                                    Constant.simpleSubTypeList);
                            mSimAdapter.updateList(mSimData, totalItem);
                            //实现recyclerView的下拉刷新
                            setOnScrollListener();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            mRefresh.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                        mRefresh.setRefreshing(false);
                    }
                });
        request.setTag(mTitle);
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
                        && lastVisibleItem + 3 > mSimAdapter.getItemCount()) {
                    if (mSimAdapter.getItemCount() - 1 < mSimAdapter.getTotal()) {
                        String urlMore = requestUrl + ("?start=" + mSimAdapter.getStart());
                        loadMore(urlMore);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem =
                        ((LinearLayoutManager) mRecView.getLayoutManager()).
                                findLastVisibleItemPosition();
                if (dy < 0 && !isShow) {
                    animatorForVisible();
                    isShow = true;
                } else if (dy > 0 && isShow) {
                    animatorForGone();
                    isShow = false;
                }
            }
        });
    }

    /**
     * adapter加载更多
     */
    private void loadMore(String url) {
        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    List<SimpleSub> moreData = new GsonBuilder().create().fromJson(
                            response.getString(JSON_SUBJECTS), Constant.simpleSubTypeList);
                    mSimAdapter.loadMoreData(moreData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(mTitle);
        MyApplication.getHttpQueue().add(request);
    }

    /**
     * 通过Volley框架的全局消息队列获取到url对应的数据
     */
    private void volley_Get_USBox() {
        mRefresh.setRefreshing(true);
        JsonObjectRequest request = new JsonObjectRequest(requestUrl,
                new Response.Listener<JSONObject>() {
                    private Gson gson = new GsonBuilder().create();

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mBoxData = gson.fromJson(response.getString(JSON_SUBJECTS),
                                    Constant.simpleBoxTypeList);
                            mBoxAdapter.updateList(mBoxData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            mRefresh.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                        mRefresh.setRefreshing(false);
                    }
                });
        request.setTag(mTitle);
        MyApplication.getHttpQueue().add(request);
    }

    @Override
    public void onStop() {
        super.onStop();
        MyApplication.getHttpQueue().cancelAll(mTitle);
    }

    /**
     * 界面跳转
     */
    private void prepareIntent(Class cla, String id) {
        Intent intent = new Intent(getActivity(), cla);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    /**
     * 为floatingActionBar的出现消失设置动画效果
     */


    private void animatorForGone() {
        Animator anim = AnimatorInflater.loadAnimator(getActivity(), R.animator.scale_gone);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mBtn.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        anim.setTarget(mBtn);
        anim.start();
    }

    private void animatorForVisible() {
        Animator anim = AnimatorInflater.loadAnimator(getActivity(), R.animator.scale_visible);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        anim.setTarget(mBtn);
        anim.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
