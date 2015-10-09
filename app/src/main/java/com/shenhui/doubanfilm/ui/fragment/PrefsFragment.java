package com.shenhui.doubanfilm.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.shenhui.doubanfilm.R;

/**
 * Created by sanousun on 2015/8/18.
 */
public class PrefsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }
}
