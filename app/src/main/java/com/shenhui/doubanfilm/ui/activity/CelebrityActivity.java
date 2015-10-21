package com.shenhui.doubanfilm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shenhui.doubanfilm.MyApplication;
import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.adapter.SubCardAdapter;
import com.shenhui.doubanfilm.bean.CastAndCommend;
import com.shenhui.doubanfilm.bean.Celebrity;
import com.shenhui.doubanfilm.support.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CelebrityActivity extends AppCompatActivity
        implements SubCardAdapter.OnItemClickListener {

    private static final String VOLLEY_TAG = "CelActivity";
    private static final String KEY_CEL_ID = "cel_id";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
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


    private String mId;
    private Celebrity mCelebrity;
    private List<CastAndCommend> mWorksData = new ArrayList<>();
    private SubCardAdapter mWorksAdapter;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().
            showImageForEmptyUri(R.drawable.noimage).
            showImageOnFail(R.drawable.noimage).
            showImageForEmptyUri(R.drawable.lks_for_blank_url).
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_celebrity);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        mId = getIntent().getStringExtra(KEY_CEL_ID);
        String url = Constant.API + Constant.CELEBRITY + mId;
        volley_get(url);
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mWorksView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 通过volley得到mCelebrity
     */
    private void volley_get(String url) {
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mCelebrity = new GsonBuilder().create().fromJson(response, Constant.cleType);
                setDataToView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CelebrityActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(VOLLEY_TAG);
        MyApplication.getHttpQueue().add(request);
    }

    /**
     * 得到mCelebrity实例后设置界面
     */
    private void setDataToView() {
        if (mCelebrity == null) return;
        getSupportActionBar().setTitle(mCelebrity.getName());
        imageLoader.displayImage(mCelebrity.getAvatars().getMedium(),
                mImage, options);
        mName.setText(mCelebrity.getName());
        mNameEn.setText(mCelebrity.getName_en());
        String gender = getResources().getString(R.string.gender);
        mGender.setText(String.format("%s%s", gender, mCelebrity.getGender()));
        String bronPlace = getResources().getString(R.string.bron_place);
        mBronPlace.setText(String.format("%s%s", bronPlace, mCelebrity.getBorn_place()));

        if (mCelebrity.getAka().size() > 0) {
            SpannableString ake = new SpannableString(getString(R.string.cel_ake));
            ake.setSpan(new ForegroundColorSpan(
                    Color.BLACK), 0, ake.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mAke.setText(ake);
            mAke.append(listToString(mCelebrity.getAka()));
        } else {
            mAke.setVisibility(View.GONE);
        }

        if (mCelebrity.getAka_en().size() > 0) {
            SpannableString ake_en = new SpannableString(getString(R.string.cel_ake_en));
            ake_en.setSpan(new ForegroundColorSpan(
                    Color.BLACK), 0, ake_en.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mAkeEn.setText(ake_en);
            mAkeEn.append(listToString(mCelebrity.getAka_en()));
        } else {
            mAkeEn.setVisibility(View.GONE);
        }

        mWorks.setText(String.format("%s的影视作品", mCelebrity.getName()));

        for (int i = 0; i < mCelebrity.getWorks().size(); i++) {
            Celebrity.WorksEntity work = mCelebrity.getWorks().get(i);
            CastAndCommend data = new CastAndCommend(
                    work.getSubject().getAlt(),
                    work.getSubject().getId(),
                    work.getSubject().getTitle(),
                    work.getSubject().getImages().getLarge());
            mWorksData.add(data);
        }
        mWorksAdapter = new SubCardAdapter(CelebrityActivity.this, mWorksData);
        mWorksAdapter.setOnItemClickListener(this);
        mWorksView.setAdapter(mWorksAdapter);

        mCelLayout.setVisibility(View.VISIBLE);
    }

    /**
     * List.toString方法
     */
    private String listToString(List<String> data) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            str.append(i == 0 ? "" : "/").append(data.get(i));
        }
        return str.toString();
    }

    @Override
    public void itemClick(String id, boolean isCom) {
        SubjectActivity.toActivity(this, id);
    }

}
