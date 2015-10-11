package com.shenhui.doubanfilm.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shenhui.doubanfilm.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sanousun on 2015/9/20.
 */
public class TopFragment extends Fragment {

    private static final String[] TITLES = {"壹至伍拾", "伍壹至壹佰", "壹佰壹至壹佰伍", "壹伍壹至贰佰", "贰佰壹至贰佰伍"};
    @Bind(R.id.tab_home)
    TabLayout mTabLayout;
    @Bind(R.id.vp_home)
    ViewPager mViewPager;

    private PagerAdapter mPagerAdapter;

    public static final String TOP_FRAGMENT_TOP = "top";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        mPagerAdapter = new TopPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabTextColors(Color.GRAY, Color.WHITE);
        mTabLayout.setTabsFromPagerAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class TopPagerAdapter extends FragmentStatePagerAdapter {

        public TopPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new TopPagerFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(TOP_FRAGMENT_TOP, position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }
}
