package com.shenhui.doubanfilm.ui.activity;

import android.app.FragmentManager;
import android.os.Bundle;

import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.ui.fragment.PrefsFragment;

public class PrefsActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    protected void initView() {
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.container,
                new PrefsFragment()).commit();
    }
}
