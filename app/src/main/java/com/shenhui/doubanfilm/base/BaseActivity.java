package com.shenhui.doubanfilm.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author dashu
 * @date 2018/3/16
 * activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {

    private CompositeDisposable mDisposables;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState,
                         @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mDisposables = new CompositeDisposable();
    }


    protected void pend(Disposable disposable) {
        mDisposables.add(disposable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing() && mDisposables != null) {
            mDisposables.clear();
        }
    }
}
