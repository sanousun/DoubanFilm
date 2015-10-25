package com.shenhui.doubanfilm.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.Window;
import android.widget.TextView;

import com.shenhui.doubanfilm.R;

public class PrefsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private static final String ABOUT = "about";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        findPreference(ABOUT).setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(ABOUT)) {
            showApacheLicenseDialog();
        }
        return false;
    }

    private void showApacheLicenseDialog() {

        Dialog apacheLicenseDialog = new Dialog(getActivity());
        apacheLicenseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        apacheLicenseDialog.setCancelable(true);
        apacheLicenseDialog.setContentView(R.layout.dialog_apache_license);
        TextView textView = (TextView) apacheLicenseDialog.findViewById(R.id.tv_dialog);
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.licences_header)).append("\n");
        String[] basedOnProjects = getResources().getStringArray(R.array.apache_licensed_projects);
        for (String str : basedOnProjects) {
            sb.append(str).append("\n");
        }
        sb.append("\n\n").append(getString(R.string.license));
        textView.setText(sb.toString());
        apacheLicenseDialog.show();
    }
}
