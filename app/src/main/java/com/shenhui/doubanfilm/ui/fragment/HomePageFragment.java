package com.shenhui.doubanfilm.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.adapter.SimpleMovieAdapter;
import com.shenhui.doubanfilm.base.BaseFragment;
import com.shenhui.doubanfilm.base.BasePullAdapter;
import com.shenhui.doubanfilm.data.LoadHelper;
import com.shenhui.doubanfilm.data.LoadUtil;
import com.shenhui.doubanfilm.databinding.FragmentHomePageBinding;
import com.shenhui.doubanfilm.ui.SpaceItemDecoration;

import io.reactivex.disposables.Disposable;

/**
 * @author dashu
 * @date 2018/3/20
 * 主页的列表展示
 */

public class HomePageFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener,
        BasePullAdapter.OnLoadListener {

    private FragmentHomePageBinding mHomePageBinding;
    private int mIndex;
    private SimpleMovieAdapter mMovieAdapter;

    public static HomePageFragment getInstance() {
        return new HomePageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mHomePageBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_home_page, container, false);
        initView();
        return mHomePageBinding.getRoot();
    }

    public void initView() {
        mHomePageBinding.recycleMain.setLayoutManager(new LinearLayoutManager(getContext()));
        mHomePageBinding.recycleMain.addItemDecoration(new SpaceItemDecoration());
        mMovieAdapter = new SimpleMovieAdapter(getContext());
        mMovieAdapter.setOnPullListener(this);
        mHomePageBinding.recycleMain.setAdapter(mMovieAdapter);
        mHomePageBinding.refreshMain.setOnRefreshListener(this);
        onRefresh();
    }

    private void loadData() {
        Disposable disposable = LoadUtil.getService()
                .getMovieInTheaters(mIndex)
                .compose(LoadHelper.transformer())
                .doOnSubscribe(subscription -> {
                    if (mIndex == 0) {
                        mHomePageBinding.refreshMain.setRefreshing(true);
                    }
                })
                .subscribe(movieList -> {
                    if (mIndex == 0) {
                        mHomePageBinding.refreshMain.setRefreshing(false);
                        mMovieAdapter.clear();
                    }
                    if (movieList == null
                            || movieList.count == 0) {
                        mMovieAdapter.showNoMore();
                    } else {
                        mMovieAdapter.rangeInsert(movieList.subjects);
                        mIndex += movieList.start + movieList.count;
                        if (movieList.start + movieList.count >= movieList.total) {
                            mMovieAdapter.showNoMore();
                        } else {
                            mMovieAdapter.showLoading();
                        }
                    }
                }, throwable -> {
                    if (mIndex == 0) {
                        mHomePageBinding.refreshMain.setRefreshing(false);
                        Toast.makeText(getContext(), "加载错误", Toast.LENGTH_SHORT).show();
                    } else {
                        mMovieAdapter.showError();
                    }
                });
        pend(disposable);
    }

    @Override
    public void onRefresh() {
        mIndex = 0;
        loadData();
    }

    @Override
    public void onLoad() {
        loadData();
    }
}
