package com.shenhui.doubanfilm.base;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.shenhui.doubanfilm.R;

/**
 * baseActivity包含了toolbar初始化及三个初始化函数
 * Created by sanousun on 2015/9/12.
 */
public class BaseActivity extends AppCompatActivity {

    protected int layoutID = R.layout.activity_base;
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        initView();
        initData();
        initEvent();
    }

    protected void initView() {

    }

    protected void initData() {

    }

    protected void initEvent() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
