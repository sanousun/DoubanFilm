package com.shenhui.doubanfilm.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.GsonBuilder;
import com.shenhui.doubanfilm.MyApplication;
import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.adapter.SearchAdapter;
import com.shenhui.doubanfilm.base.BaseAdapter;
import com.shenhui.doubanfilm.bean.SimpleSubjectBean;
import com.shenhui.doubanfilm.support.Constant;
import com.shenhui.doubanfilm.ui.widget.IzzySearchView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity
        implements BaseAdapter.OnItemClickListener {

    private static final String VOLLEY_TAG = "SearchActivity";
    private static final String JSON_SUBJECTS = "subjects";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.rv_search)
    RecyclerView mRecyclerView;

    private SearchAdapter mAdapter;
    private List<SimpleSubjectBean> mData;
    //SearchView on the Toolbar;
    private IzzySearchView mSearchView;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mSearchView = new IzzySearchView(SearchActivity.this);
        mSearchView.setQueryHint(getString(R.string.query_hint));
        mSearchView.setOnQueryTextListener(new IzzySearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final String url;
                try {
                    url = Constant.API + Constant.SEARCH_Q + URLEncoder.encode(query, "UTF-8");
                    getDataFromUrl(url);
                    if (mDialog == null) {
                        mDialog = new ProgressDialog(SearchActivity.this);
                        mDialog.setMessage(getString(R.string.search_message));
                        mDialog.setCancelable(true);
                        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                MyApplication.getHttpQueue().cancelAll(url);
                            }
                        });
                    }
                    mDialog.show();
                    mSearchView.clearFocus();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.addView(mSearchView);

        mToolbar.addView(relativeLayout);
        setSupportActionBar(mToolbar);
        //给左上角图标的左边加上一个返回的图标.对应ActionBar.DISPLAY_HOME_AS_UP
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_search);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
    }

    private void getDataFromUrl(String url) {
        final String no_result = getString(R.string.search_no_result);
        final String error_result = getString(R.string.search_error);
        JsonObjectRequest request = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String subjects = response.getString(JSON_SUBJECTS);
                            mData = new GsonBuilder().create().fromJson(subjects,
                                    Constant.simpleSubTypeList);
                            if (mDialog != null) {
                                mDialog.dismiss();
                                mDialog = null;
                            }
                            if (mData.size() < 1) {
                                Toast.makeText(SearchActivity.this, no_result,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                mAdapter = new SearchAdapter(SearchActivity.this, mData);
                                mAdapter.setOnItemClickListener(SearchActivity.this);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (mDialog != null) {
                            mDialog.dismiss();
                            mDialog = null;
                        }
                        Toast.makeText(SearchActivity.this, error_result,
                                Toast.LENGTH_SHORT).show();
                    }
                });
        request.setTag(VOLLEY_TAG);
        MyApplication.getHttpQueue().add(request);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueue().cancelAll(VOLLEY_TAG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return true;
    }

    @Override
    public void onItemClick(String id, String imageUrl) {
        SubjectActivity.toActivity(this, id, imageUrl);
    }
}
