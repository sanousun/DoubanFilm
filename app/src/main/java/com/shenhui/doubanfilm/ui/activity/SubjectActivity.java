package com.shenhui.doubanfilm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shenhui.doubanfilm.MyApplication;
import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.adapter.SimpleFilmAdapter;
import com.shenhui.doubanfilm.bean.CelebrityEntity;
import com.shenhui.doubanfilm.bean.SimpleCardBean;
import com.shenhui.doubanfilm.bean.SimpleSubjectBean;
import com.shenhui.doubanfilm.bean.SubjectBean;
import com.shenhui.doubanfilm.support.Constant;
import com.shenhui.doubanfilm.support.util.DensityUtil;
import com.shenhui.doubanfilm.support.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SubjectActivity extends AppCompatActivity
        implements SimpleFilmAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
        AppBarLayout.OnOffsetChangedListener, View.OnClickListener {
    //intent中subjectId的key用于查询数据
    private static final String KEY_SUBJECT_ID = "subject_id";
    //json中subject的标签
    private static final String JSON_SUBJECTS = "subjects";

    private static final String URI_FOR_FILE = "file:/";
    private static final String URI_FOR_IMAGE = ".png";

    private int[] cast_id = {R.id.view_cast_layout_1, R.id.view_cast_layout_2,
            R.id.view_cast_layout_3, R.id.view_cast_layout_4,
            R.id.view_cast_layout_5, R.id.view_cast_layout_6};

    private List<View> cast_view = new ArrayList<>();

    @Bind(R.id.refresh_subj)
    SwipeRefreshLayout mRefresh;
    @Bind(R.id.btn_subj_skip)
    FloatingActionButton mBtn;

    //film header
    @Bind(R.id.appbarLayout_subj)
    AppBarLayout mAppBarLayout;
    @Bind(R.id.toolbarLayout_subj)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.iv_header_subj)
    ImageView mToolbarImage;
    @Bind(R.id.ll_subj_content)
    LinearLayout mLinearContent;
    @Bind(R.id.toolbar_subj)
    Toolbar mToolbar;
    @Bind(R.id.iv_subj_images)
    ImageView mImage;
    @Bind(R.id.rb_subj_rating)
    RatingBar mRatingBar;
    @Bind(R.id.tv_subj_rating)
    TextView mRating;
    @Bind(R.id.tv_subj_collect_count)
    TextView mCollect;
    @Bind(R.id.tv_subj_title)
    TextView mTitle;
    @Bind(R.id.tv_subj_original_title)
    TextView mOriginal_title;
    @Bind(R.id.tv_subj_genres)
    TextView mGenres;
    @Bind(R.id.tv_subj_ake)
    TextView mAke;
    @Bind(R.id.tv_subj_countries)
    TextView mCountries;

    @Bind(R.id.ll_subj_film)
    LinearLayout mFilmLayout;
    //film summary
    @Bind(R.id.tv_subj_summary)
    TextView mSummaryText;

    //film recommend
    @Bind(R.id.tv_subj_recommend_tip)
    TextView mRecommendTip;
    @Bind(R.id.re_subj_recommend)
    RecyclerView mRecommend;

    //film subject
    private String mId;
    private String mContent;
    private SubjectBean mSubject;

    private String mRecommendTags;
    private List<SimpleCardBean> mRecommendData = new ArrayList<>();
    private SimpleFilmAdapter mRecommendCardAdapter;

    private boolean isSummaryShow = false;

    private File mFile;

    private boolean isCollect = false;

    private int mImageWidth;
    private FrameLayout.LayoutParams mContentParams;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().
            showImageForEmptyUri(R.drawable.noimage).
            showImageOnFail(R.drawable.noimage).
            showImageForEmptyUri(R.drawable.lks_for_blank_url).
            cacheInMemory(true).
            cacheOnDisk(true).
            considerExifParams(true).
            build();

    //----------------------------------------------------------------------------------------

    public static void toActivity(Context context, String id) {
        Intent intent = new Intent(context, SubjectActivity.class);
        intent.putExtra(KEY_SUBJECT_ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        ButterKnife.bind(this);
        initView();
        initEvent();
        Intent intent = getIntent();
        mId = intent.getStringExtra(KEY_SUBJECT_ID);
        mFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                mId + URI_FOR_IMAGE);
        mSubject = MyApplication.getDataSource().filmOfId(mId);
        if (mSubject != null) {
            isCollect = true;
            initAfterGetData();
        } else {
            volleyGetSubject();
        }
    }

    private void initView() {
        //设置圆形刷新球的偏移量
        mRefresh.setProgressViewOffset(false,
                -DensityUtil.dp2px(getApplication(), 8f),
                DensityUtil.dp2px(getApplication(), 32f));
        mToolbar.setTitle("");

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //用于collapsingToolbar缩放时content中内容和图片的动作
        mImageWidth = mImage.getLayoutParams().width + DensityUtil.dp2px(getApplication(), 8f);
        mContentParams = (FrameLayout.LayoutParams) mLinearContent.getLayoutParams();

        for (int id : cast_id) {
            View view = findViewById(id);
            view.setVisibility(View.GONE);
            view.setOnClickListener(this);
            cast_view.add(view);
        }

        mRecommend.setLayoutManager(new LinearLayoutManager(
                SubjectActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mRecommendCardAdapter = new SimpleFilmAdapter(this, mRecommendData);
        mRecommend.setAdapter(mRecommendCardAdapter);
    }

    private void initEvent() {
        mRefresh.setOnRefreshListener(this);
        mBtn.setOnClickListener(this);
        mRecommendCardAdapter.setOnItemClickListener(this);
        mRecommendTip.setOnClickListener(this);
        mRecommendTip.setClickable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //利用appBarLayout的回调接口禁止或启用swipeRefreshLayout
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAppBarLayout.removeOnOffsetChangedListener(this);
    }

    private void volleyGetSubject() {
        String url = Constant.API + Constant.SUBJECT + mId;
        mRefresh.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        mContent = response;
                        //如果film已经收藏,更新数据
                        if (isCollect) {
                            filmSave();
                        }
                        mSubject = new Gson().fromJson(mContent,
                                Constant.subType);
                        initAfterGetData();
                        mRefresh.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SubjectActivity.this,
                                error.toString(),
                                Toast.LENGTH_SHORT).show();
                        mRefresh.setRefreshing(false);
                    }
                });
        stringRequest.setTag(mId);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getHttpQueue().add(stringRequest);
    }


    /**
     * 得到网络返回数据初始化界面
     */
    private void initAfterGetData() {
        if (mSubject == null) return;
        if (mSubject.getTitle() != null) {
            mCollapsingToolbarLayout.setTitle(mSubject.getTitle());
        }
        String imageUri = (mFile.exists() ?
                String.format("%s%s", URI_FOR_FILE, mFile.getPath()) :
                mSubject.getImages().getLarge());
        imageLoader.displayImage(imageUri, mImage, options);
        imageLoader.displayImage(imageUri, mToolbarImage, options,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        Palette.from(loadedImage).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                int defaultBgColor = Color.parseColor("#009688");
                                int bgColor = palette.getDarkVibrantColor(defaultBgColor);
                                mCollapsingToolbarLayout.setBackgroundColor(bgColor);
                            }
                        });
                    }
                });

        //豆瓣抽风不给评分了::>_<::
        if (mSubject.getRating() != null) {
            float rate = (float) (mSubject.getRating().getAverage() / 2);
            mRatingBar.setRating(rate);
            mRating.setText(String.format("%s", rate * 2));
        }

        mCollect.setText(
                String.format("%s%d%s",
                        getString(R.string.collect),
                        mSubject.getCollect_count(),
                        getString(R.string.count)));
        mTitle.setText(String.format("%s   ", mSubject.getTitle()));
        SpannableString year = new SpannableString(
                String.format("  %s  ", mSubject.getYear()));
        year.setSpan(new ForegroundColorSpan(Color.WHITE),
                0, year.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        year.setSpan(new BackgroundColorSpan(Color.parseColor("#5ea4ff")),
                0, year.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        year.setSpan(new RelativeSizeSpan(0.7f),
                0, year.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTitle.append(year);

        if (!mSubject.getOriginal_title().equals(mSubject.getTitle())) {
            mOriginal_title.setText(mSubject.getOriginal_title());
            mOriginal_title.setVisibility(View.VISIBLE);
        } else {
            mOriginal_title.setVisibility(View.GONE);
        }
        mGenres.setText(StringUtil.getListString(mSubject.getGenres(), ','));
        mAke.setText(StringUtil.getSpannableString(
                getString(R.string.ake), Color.GRAY));
        mAke.append(StringUtil.getListString(mSubject.getAka(), '/'));
        mCountries.setText(StringUtil.getSpannableString(
                getString(R.string.countries), Color.GRAY));
        mCountries.append(StringUtil.getListString(mSubject.getCountries(), '/'));

        mSummaryText.setText(StringUtil.getSpannableString(
                getString(R.string.summary), Color.parseColor("#5ea4ff")));
        mSummaryText.append(mSubject.getSummary());
        mSummaryText.setEllipsize(TextUtils.TruncateAt.END);
        mSummaryText.setOnClickListener(this);
        //获得导演演员数据列表
        int j = 0;
        boolean isDirWithCast = false;
        for (int i = 0; (i < mSubject.getDirectors().size() && i < 2); i++) {
            View view = cast_view.get(i);
            CelebrityEntity cel = mSubject.getDirectors().get(j++);
            ImageView iv = (ImageView) view.findViewById(R.id.iv_item_simple_cast_image);
            imageLoader.displayImage(cel.getAvatars().getLarge(), iv, options);
            TextView tv = (TextView) view.findViewById(R.id.tv_item_simple_cast_text);
            if (i == 0 && cel.getId().equals(mSubject.getCasts().get(0).getId())) {
                tv.setText(cel.getName() + getString(R.string.dir_and_cast));
                isDirWithCast = true;
            } else {
                tv.setText(cel.getName() + getString(R.string.director));
            }
            view.setVisibility(View.VISIBLE);
        }

        j = 2;
        for (int i = 0; (i < mSubject.getCasts().size() && i < 4); i++) {
            if (i == 0 && isDirWithCast) {
                i++;
            }
            View view = cast_view.get(j++);
            CelebrityEntity cel = mSubject.getCasts().get(i);
            ImageView iv = (ImageView) view.findViewById(R.id.iv_item_simple_cast_image);
            imageLoader.displayImage(cel.getAvatars().getLarge(), iv, options);
            TextView tv = (TextView) view.findViewById(R.id.tv_item_simple_cast_text);
            tv.setText(cel.getName());
            view.setVisibility(View.VISIBLE);
        }
        //显示View
        mFilmLayout.setVisibility(View.VISIBLE);
        //加载推荐
        mRecommendTip.setText(getString(R.string.recommend_loading));
        StringBuilder tag = new StringBuilder();
        for (int i = 0; i < mSubject.getGenres().size(); i++) {
            tag.append(mSubject.getGenres().get(i));
            if (i == 1) break;
        }
        mRecommendTags = tag.toString();
        volley_Get_Recommend();
    }

    /**
     * 通过查询tag获得recommend数据
     */
    private void volley_Get_Recommend() {

        if (TextUtils.isEmpty(mRecommendTags)) return;
        String url = Constant.API + Constant.SEARCH_TAG + mRecommendTags;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new GsonBuilder().create();
                        try {
                            mRecommendTip.setText(getString(R.string.recommend_list));
                            mRecommendTip.setClickable(false);
                            String json = response.getString(JSON_SUBJECTS);
                            List<SimpleSubjectBean> data = gson.fromJson(json,
                                    Constant.simpleSubTypeList);
                            mRecommendData = new ArrayList<>();
                            for (SimpleSubjectBean simpleSub : data) {
                                mRecommendData.add(new SimpleCardBean(
                                        simpleSub.getId(),
                                        simpleSub.getTitle(),
                                        simpleSub.getImages().getLarge(),
                                        true));
                            }
                            mRecommendCardAdapter.updateData(mRecommendData);
                            mRecommend.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mRecommendTip.setText(getString(R.string.recommend_load_fail));
                        mRecommendTip.setClickable(true);
                    }
                });
        request.setTag(mId);
        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getHttpQueue().add(request);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueue().cancelAll(mId);
    }

    @Override
    public void itemClick(String id, boolean isFilm) {
        if (isFilm) {
            SubjectActivity.toActivity(this, id);
        } else {
            CelebrityActivity.toActivity(this, id);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sub, menu);
        MenuItem collect = menu.findItem(R.id.action_sub_collect);
        if (isCollect) {
            collect.setIcon(R.drawable.ic_action_collected);
        } else {
            collect.setIcon(R.drawable.ic_action_uncollected);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.action_sub_search:
                this.startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.action_sub_collect:
                collectFilmAndSaveImage();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 点击收藏后将subject存入数据库中,并将图片存入文件
     */
    private void collectFilmAndSaveImage() {
        if (mSubject == null) return;
        if (isCollect) {
            cancelSave();
            isCollect = false;
        } else {
            filmSave();
            isCollect = true;
        }
        supportInvalidateOptionsMenu();
    }

    /**
     * 用于保存filmContent和filmImage
     */
    private void filmSave() {
        if (mFile.exists()) {
            mFile.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(mFile);
            Bitmap bitmap = imageLoader.loadImageSync(mSubject.getImages().getLarge());
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将电影信息存入到数据库中
        mSubject.setLocalImageFile(mFile.getPath());
        String content = new Gson().toJson(mSubject, Constant.subType);
        MyApplication.getDataSource().insertOrUpDataFilm(mId, content);
        Toast.makeText(this, getString(R.string.collect_completed), Toast.LENGTH_SHORT).show();
    }

    private void cancelSave() {
        MyApplication.getDataSource().deleteFilm(mId);
        if (mFile.exists()) {
            mFile.delete();
        }
        Toast.makeText(this, R.string.collect_cancel, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        volleyGetSubject();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        //利用AppBarLayout的回调接口启用或者关闭滑动刷新
        mRefresh.setEnabled(i == 0);
        float alpha = (-1.0f * i) /
                (appBarLayout.getHeight() - mToolbar.getHeight());
        changeContentLayout(alpha);
    }

    /**
     * 使content中内容位置和图片透明度随着AppBarLayout的伸缩而改变
     */
    private void changeContentLayout(float a) {
        setContentGravity(a == 1 ? Gravity.START : Gravity.CENTER_HORIZONTAL);
        mImage.setAlpha(a);
        mContentParams.leftMargin = (int) (mImageWidth * a);
        mLinearContent.setLayoutParams(mContentParams);
    }

    private void setContentGravity(int gravity) {
        mLinearContent.setGravity(gravity);
        mAke.setGravity(gravity);
        mCountries.setGravity(gravity);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_subj_summary:
                if (isSummaryShow) {
                    isSummaryShow = false;
                    mSummaryText.setEllipsize(TextUtils.TruncateAt.END);
                    mSummaryText.setLines(3);
                } else {
                    isSummaryShow = true;
                    mSummaryText.setEllipsize(null);
                    mSummaryText.setSingleLine(false);
                }
                break;
            case R.id.btn_subj_skip://跳往豆瓣电影的移动版网页
                if (mSubject == null) break;
                WebActivity.toWebActivity(this,
                        mSubject.getMobile_url(), mSubject.getTitle());
                break;
            case R.id.tv_subj_recommend_tip:
                volley_Get_Recommend();
                break;
            case R.id.view_cast_layout_1:
                CelebrityActivity.toActivity(this, mSubject.getDirectors().get(0).getId());
                break;
            case R.id.view_cast_layout_2:
                CelebrityActivity.toActivity(this, mSubject.getDirectors().get(1).getId());
                break;
            case R.id.view_cast_layout_3:
                CelebrityActivity.toActivity(this, mSubject.getCasts().get(0).getId());
                break;
            case R.id.view_cast_layout_4:
                CelebrityActivity.toActivity(this, mSubject.getCasts().get(1).getId());
                break;
            case R.id.view_cast_layout_5:
                CelebrityActivity.toActivity(this, mSubject.getCasts().get(2).getId());
                break;
            case R.id.view_cast_layout_6:
                CelebrityActivity.toActivity(this, mSubject.getCasts().get(3).getId());
                break;
        }
    }
}
