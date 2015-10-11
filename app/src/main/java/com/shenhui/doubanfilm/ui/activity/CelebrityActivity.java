package com.shenhui.doubanfilm.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shenhui.doubanfilm.MyApplication;
import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.adapter.SubCardAdapter;
import com.shenhui.doubanfilm.bean.CastAndCommend;
import com.shenhui.doubanfilm.bean.Celebrity;
import com.shenhui.doubanfilm.support.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sanousun on 2015/9/12.
 */
public class CelebrityActivity extends AppCompatActivity
        implements SubCardAdapter.OnItemClickListener {

    private static final String VOLLEY_TAG = "CelActivity";

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
    private ImageLoadingListener imageLoadingListener = new AnimateFirstDisplayListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_celebrity);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        mId = getIntent().getStringExtra("id");
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
     *
     * @param url
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
                mImage, options, imageLoadingListener);
        mName.setText(mCelebrity.getName());
        mNameEn.setText(mCelebrity.getName_en());
        String gender = getResources().getString(R.string.gender);
        mGender.setText(gender + mCelebrity.getGender());
        String bronPlace = getResources().getString(R.string.bron_place);
        mBronPlace.setText(bronPlace + mCelebrity.getBorn_place());

        if (mCelebrity.getAka().size() > 0) {
            SpannableString ake = new SpannableString("更多中文名：");
            ake.setSpan(new ForegroundColorSpan(
                    Color.BLACK), 0, ake.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mAke.setText(ake);
            mAke.append(listToString(mCelebrity.getAka()));
        } else {
            mAke.setVisibility(View.GONE);
        }

        if (mCelebrity.getAka_en().size() > 0) {
            SpannableString ake_en = new SpannableString("更多英文名：");
            ake_en.setSpan(new ForegroundColorSpan(
                    Color.BLACK), 0, ake_en.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mAkeEn.setText(ake_en);
            mAkeEn.append(listToString(mCelebrity.getAka_en()));
        } else {
            mAkeEn.setVisibility(View.GONE);
        }

        mWorks.setText(mCelebrity.getName() + "的影视作品");

        for (int i = 0; i < mCelebrity.getWorks().size(); i++) {
            Celebrity.WorksEntity work = mCelebrity.getWorks().get(i);
            CastAndCommend data = new CastAndCommend(
                    work.getSubject().getAlt(),
                    work.getSubject().getId(),
                    work.getSubject().getTitle(),
                    work.getSubject().getImages().getLarge(),
                    work.getRoles().equals("导演"));
            mWorksData.add(data);
        }
        mWorksAdapter = new SubCardAdapter(CelebrityActivity.this, mWorksData);
        mWorksAdapter.setOnItemClickListener(this);
        mWorksView.setAdapter(mWorksAdapter);
    }

    /**
     * List.toString方法
     *
     * @param data
     * @return
     */
    private String listToString(List<String> data) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < data.size(); i++) {
            s.append((i == 0 ? "" : "/") + data.get(i));
        }
        return s.toString();
    }

    @Override
    public void itemClick(String id, boolean isCom) {

        Intent intent = new Intent(CelebrityActivity.this, SubjectActivity.class);
        intent.putExtra("id", id);
        this.startActivity(intent);
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
