package com.shenhui.doubanfilm.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import java.util.ArrayList;
import java.util.List;


/**
 * FragmentStatePagerAdapter的缓存问题，无法缓存fragment
 *
 * Created by sanousun on 2015/9/17.
 */
public class HomeFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    private static String[] TITLES = {"正在热映", "即将上映", "北美票房"};
    public static final String HOME_FRAGMENT_TITLE = "title";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_home);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_home);
        initData();
        return view;
    }

    private void initData() {
        mPagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabTextColors(Color.GRAY, Color.WHITE);
        mTabLayout.setTabsFromPagerAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private class HomePagerAdapter extends FragmentStatePagerAdapter {

        public HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new HomePagerFragment();
            Bundle bundle = new Bundle();
            bundle.putString(HOME_FRAGMENT_TITLE, TITLES[position]);
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
