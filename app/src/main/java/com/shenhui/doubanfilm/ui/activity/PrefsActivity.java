package com.shenhui.doubanfilm.ui.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.base.BaseActivity;
import com.shenhui.doubanfilm.ui.fragment.PrefsFragment;


/**
 * 增加网络设置
 * Created by sanousun on 2015/8/19.
 */
public class PrefsActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.container, new PrefsFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
