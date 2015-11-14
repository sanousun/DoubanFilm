package com.shenhui.doubanfilm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shenhui.doubanfilm.MyApplication;
import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.adapter.SimpleFilmAdapter;
import com.shenhui.doubanfilm.base.BaseActivity;
import com.shenhui.doubanfilm.bean.SimpleCardBean;
import com.shenhui.doubanfilm.bean.CelebrityBean;
import com.shenhui.doubanfilm.bean.WorksEntity;
import com.shenhui.doubanfilm.support.Constant;
import com.shenhui.doubanfilm.support.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CelebrityActivity extends BaseActivity
        implements SimpleFilmAdapter.OnItemClickListener {

    private static final String VOLLEY_TAG = "CelActivity";
    private static final String KEY_CEL_ID = "cel_id";

    @Bind(R.id.tv_cel_name)
    TextView mName;
    @Bind(R.id.tv_cel_name_en)
    TextView mNameEn;
    @Bind(R.id.tv_cel_gender)
    TextView mGender;
    @Bind(R.id.tv_cel_bron_place)
    TextView mBronPlace;
    @Bind(R.id.tv_cel_ake)
    TextView mAke;
    @Bind(R.id.tv_cel_ake_en)
    TextView mAkeEn;
    @Bind(R.id.iv_cel_image)
    ImageView mImage;
    @Bind(R.id.tv_cel_works)
    TextView mWorks;
    @Bind(R.id.rv_cel_works)
    RecyclerView mWorksView;
    @Bind(R.id.ll_cel_layout)
    LinearLayout mCelLayout;


    private CelebrityBean mCelebrity;
    private List<SimpleCardBean> mWorksData = new ArrayList<>();

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().
            showImageOnLoading(R.drawable.noimage).
            showImageForEmptyUri(R.drawable.noimage).
            showImageOnFail(R.drawable.noimage).
            cacheInMemory(true).
            cacheOnDisk(true).
            considerExifParams(true).
            build();

    public static void toActivity(Context context, String id) {
        Intent intent = new Intent(context, CelebrityActivity.class);
        intent.putExtra(KEY_CEL_ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutID = R.layout.activity_celebrity;
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        mWorksView.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.HORIZONTAL, false));
    }

    private void initData() {
        String mId = getIntent().getStringExtra(KEY_CEL_ID);
        String url = Constant.API + Constant.CELEBRITY + mId;
        volleyGetCelebrity(url);
    }

    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueue().cancelAll(VOLLEY_TAG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cel_search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            case R.id.action_cel_skip:
                if (mCelebrity == null) {
                    return true;
                }
                WebActivity.toWebActivity(this,
                        mCelebrity.getMobile_url(),
                        mCelebrity.getName());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 通过volley得到mCelebrity
     */
    private void volleyGetCelebrity(String url) {
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mCelebrity = new GsonBuilder().create().fromJson(response,
                        Constant.cleType);
                setViewAfterGetData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CelebrityActivity.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(VOLLEY_TAG);
        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getHttpQueue().add(request);
    }

    /**
     * 得到mCelebrity实例后设置界面
     */
    private void setViewAfterGetData() {
        if (mCelebrity == null) return;
        setActionBarTitle(mCelebrity.getName());
        imageLoader.displayImage(mCelebrity.getAvatars().getMedium(),
                mImage, options);
        mName.setText(mCelebrity.getName());
        mNameEn.setText(mCelebrity.getName_en());
        String gender = getResources().getString(R.string.gender);
        mGender.setText(String.format("%s%s", gender,
                mCelebrity.getGender()));
        String bronPlace = getResources().getString(R.string.bron_place);
        mBronPlace.setText(String.format("%s%s", bronPlace,
                mCelebrity.getBorn_place()));

        if (mCelebrity.getAka().size() > 0) {
            mAke.setText(StringUtil.getSpannableString(
                    getString(R.string.cel_ake), Color.BLACK));
            mAke.append(StringUtil.getListString(mCelebrity.getAka(), '/'));
        } else {
            mAke.setVisibility(View.GONE);
        }

        if (mCelebrity.getAka_en().size() > 0) {
            mAkeEn.setText(StringUtil.getSpannableString(
                    getString(R.string.cel_ake_en), Color.BLACK));
            mAkeEn.append(StringUtil.getListString(mCelebrity.getAka_en(), '/'));
        } else {
            mAkeEn.setVisibility(View.GONE);
        }

        mWorks.setText(String.format("%s的影视作品",
                mCelebrity.getName()));

        for (WorksEntity work : mCelebrity.getWorks()) {
            SimpleCardBean data = new SimpleCardBean(
                    work.getSubject().getId(),
                    work.getSubject().getTitle(),
                    work.getSubject().getImages().getLarge(),
                    true);
            mWorksData.add(data);
        }
        SimpleFilmAdapter mWorksAdapter =
                new SimpleFilmAdapter(CelebrityActivity.this, mWorksData);
        mWorksAdapter.setOnItemClickListener(this);
        mWorksView.setAdapter(mWorksAdapter);

        mCelLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void itemClick(String id, boolean isCom) {
        SubjectActivity.toActivity(this, id);
    }

}
