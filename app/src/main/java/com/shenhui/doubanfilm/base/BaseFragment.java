package com.shenhui.doubanfilm.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author dashu
 * @date 2018/3/20
 * 基础fragment定义
 */

public class BaseFragment extends Fragment {

    private CompositeDisposable mDisposables;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDisposables = new CompositeDisposable();
    }

    protected void pend(Disposable disposable) {
        mDisposables.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposables != null) {
            mDisposables.clear();
        }
    }
}
