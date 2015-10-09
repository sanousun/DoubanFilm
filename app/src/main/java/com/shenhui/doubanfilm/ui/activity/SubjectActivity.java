package com.shenhui.doubanfilm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shenhui.doubanfilm.MyApplication;
import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.adapter.CastAdapter;
import com.shenhui.doubanfilm.adapter.SubCardAdapter;
import com.shenhui.doubanfilm.bean.CastAndCommend;
import com.shenhui.doubanfilm.bean.SimpleSub;
import com.shenhui.doubanfilm.bean.Subject;
import com.shenhui.doubanfilm.support.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by sanousun on 2015/9/4.
 */
public class SubjectActivity extends AppCompatActivity
        implements CastAdapter.OnItemClickListener,
        SubCardAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
        AppBarLayout.OnOffsetChangedListener {

    private static final String JSON_SUBJECTS = "subjects";

    private SwipeRefreshLayout mRefresh;

    //film header
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView mToolbarImage;
    private Toolbar mToolbar;
    private ImageView mImage;
    private RatingBar mRatingBar;
    private TextView mRating;
    private TextView mYear;
    private TextView mCollect;
    private TextView mTitle;
    private TextView mOriginal_title;
    private TextView mGenres;
    private TextView mAke;
    private TextView mCountries;

    //film summary
    private CardView mSummary;
    private TextView mSummaryText;

    //film director and cast
    private RecyclerView mCast;

    //film recommend
    private RecyclerView mRecommend;

    //film subject
    private String mId;
    private String mContent;
    private Subject mSubject;
    private List<CastAndCommend> mCastData = new ArrayList<>();
    private List<CastAndCommend> mCommendData = new ArrayList<>();

    private boolean isSummaryShow = false;
    private CastAdapter mCastAdapter;
    private SubCardAdapter mCommendAdapter;

    private File mFile;

    private Semaphore mCollectSemaphore = new Semaphore(0);
    private boolean isCollect = false;

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
        setContentView(R.layout.layout_subject);
        initView();
        Intent intent = getIntent();
        mId = intent.getStringExtra("id");
        mFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mId + ".jpg");
        Log.i("xyz", mFile.getPath());
        mSubject = MyApplication.getDataSource().filmOfId(mId);
        if (mSubject != null) {
            isCollect = true;
            initAfterGetData();
        } else {
            volley_GET();
        }
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

    private void initView() {

        mRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh_subj);
        mRefresh.setProgressViewOffset(false, 0, 100);
        mRefresh.setOnRefreshListener(this);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbarLayout_subj);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbarLayout_subj);
        mToolbarImage = (ImageView) findViewById(R.id.iv_header_subj);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_subj);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mImage = (ImageView) findViewById(R.id.iv_subj_images);
        mRatingBar = (RatingBar) findViewById(R.id.rb_subj_rating);
        mRating = (TextView) findViewById(R.id.tv_subj_rating);
        mYear = (TextView) findViewById(R.id.tv_subj_year);
        mCollect = (TextView) findViewById(R.id.tv_subj_collect_count);
        mTitle = (TextView) findViewById(R.id.tv_subj_title);
        mOriginal_title = (TextView) findViewById(R.id.tv_subj_original_title);
        mGenres = (TextView) findViewById(R.id.tv_subj_genres);
        mAke = (TextView) findViewById(R.id.tv_subj_ake);
        mCountries = (TextView) findViewById(R.id.tv_subj_countries);
        mSummary = (CardView) findViewById(R.id.card_subj_summary);
        mSummaryText = (TextView) findViewById(R.id.tv_subj_summary);
        //由于使用了scrollView，所以必须对RecyclerView进行高度绑定
        mCast = (RecyclerView) findViewById(R.id.re_subj_cast);
        mRecommend = (RecyclerView) findViewById(R.id.re_subj_recommend);
        LinearLayoutManager manager = new LinearLayoutManager(SubjectActivity.this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mCast.setLayoutManager(manager);
        mRecommend.setLayoutManager(
                new LinearLayoutManager(
                        SubjectActivity.this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void volley_GET() {
        String url = Constant.API + Constant.SUBJECT + mId;
        mRefresh.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        mContent = response;
                        //如果film已经收藏,更新数据
                        if (isCollect) {
                            saveFilm();
                        }
                        mSubject = new Gson().fromJson(mContent, Constant.subType);
                        initAfterGetData();
                        mRefresh.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SubjectActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        mRefresh.setRefreshing(false);
                    }
                });
        stringRequest.setTag(mId);
        MyApplication.getHttpQueue().add(stringRequest);
    }


    /**
     * 得到网络返回数据初始化界面
     */
    private void initAfterGetData() {

        if (mSubject.getTitle() != null) {
            mCollapsingToolbarLayout.setTitle(mSubject.getTitle());
        }
        if (mFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(mFile.getPath());
            mImage.setImageBitmap(bitmap);
            mToolbarImage.setImageBitmap(bitmap);
        } else {
            imageLoader.displayImage(mSubject.getImages().getLarge(),
                    mImage, options, imageLoadingListener);
            imageLoader.displayImage(mSubject.getImages().getLarge(),
                    mToolbarImage, options, imageLoadingListener);
        }
        float rate = ((float) mSubject.getRating().getAverage()) / 2;
        mRatingBar.setRating(rate);
        mRating.setText((rate * 2) + "");
        mYear.setText(mSubject.getYear());
        String coll = getResources().getString(R.string.collect);
        String count = getResources().getString(R.string.count);
        mCollect.setText(coll + mSubject.getCollect_count() + count);
        mTitle.setText(mSubject.getTitle());
        if (!mSubject.getOriginal_title().equals(mSubject.getTitle())) {
            mOriginal_title.setText(mSubject.getOriginal_title());
            mOriginal_title.setVisibility(View.VISIBLE);
        } else {
            mOriginal_title.setVisibility(View.GONE);
        }
        mGenres.setText(listToString(mSubject.getGenres()));
        SpannableString ake = new SpannableString(getResources().getString(R.string.ake));
        ake.setSpan(new ForegroundColorSpan(Color.GRAY),
                0, ake.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mAke.setText(ake);
        mAke.append(listToString(mSubject.getAka()));
        SpannableString counties = new SpannableString(getResources().getString(R.string.countries));
        counties.setSpan(new ForegroundColorSpan(Color.GRAY),
                0, counties.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mCountries.setText(counties);
        mCountries.append("/n" + listToString(mSubject.getCountries()));

        //简介，点击查看具体内容
        //SpannableString可以设置文字的样式，也可以通过ImageSpan在TextView中插入图片
        //还可以通过Character span = new UnderlineSpan();设置下划线
        SpannableString summary = new SpannableString("简介：");
        summary.setSpan(new ForegroundColorSpan(Color.BLACK),
                0, summary.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mSummaryText.setText(summary);
        mSummaryText.append(mSubject.getSummary());
        mSummaryText.setEllipsize(TextUtils.TruncateAt.END);
        mSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSummaryShow) {
                    isSummaryShow = false;
                    mSummaryText.setEllipsize(TextUtils.TruncateAt.END);
                    mSummaryText.setLines(3);
                } else {
                    isSummaryShow = true;
                    mSummaryText.setEllipsize(null);
                    mSummaryText.setSingleLine(false);
                }
            }
        });

        //获得导演演员数据列表
        for (Subject.DirectorsEntity dirs : mSubject.getDirectors()) {
            CastAndCommend dir = new CastAndCommend(dirs.getAlt(), dirs.getId(), dirs.getName(),
                    dirs.getAvatars().getMedium(), true);
            mCastData.add(dir);
        }
        for (Subject.CastsEntity cas : mSubject.getCasts()) {
            CastAndCommend ca = new CastAndCommend(cas.getAlt(), cas.getId(), cas.getName(),
                    cas.getAvatars().getMedium(), false);
            mCastData.add(ca);
        }

        mCastAdapter = new CastAdapter(SubjectActivity.this, mCastData);
        mCastAdapter.setOnItemClickListener(SubjectActivity.this);
        mCast.setAdapter(mCastAdapter);


        //释放一个信号量
        mCollectSemaphore.release();
        String tag = "";
        for (int i = 0; i < mSubject.getGenres().size(); i++) {
            tag += mSubject.getGenres().get(i);
            if (i == 1) break;
        }
        volley_rem_GET(tag);
    }

    /**
     * 通过查询tag获得recommend数据
     */
    private void volley_rem_GET(String tag) {

        String url = Constant.API + Constant.SEARCH_TAG + tag;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new GsonBuilder().create();
                        try {
                            String json = response.getString(JSON_SUBJECTS);
                            List<SimpleSub> data = gson.fromJson(json, Constant.simpleSubTypeList);
                            for (SimpleSub simpleSub : data) {
                                mCommendData.add(new CastAndCommend(simpleSub.getAlt(), simpleSub.getId(),
                                        simpleSub.getTitle(), simpleSub.getImages().getLarge()));
                            }
                            mCommendAdapter = new SubCardAdapter(SubjectActivity.this, mCommendData);
                            mCommendAdapter.setOnItemClickListener(SubjectActivity.this);
                            mRecommend.setAdapter(mCommendAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SubjectActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        request.setTag(mId);
        MyApplication.getHttpQueue().add(request);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueue().cancelAll(mId);
    }

    @Override
    public void itemClick(String id, boolean isCom) {
        if (isCom) {
            prepareIntent(SubjectActivity.this, SubjectActivity.class, id);
        } else {
            prepareIntent(SubjectActivity.this, CelebrityActivity.class, id);
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
                collectFilm();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 点击收藏后将subject存入数据库中,并将图片存入文件
     */
    private synchronized void collectFilm() {
        if (mSubject == null) return;
        if (isCollect) {
            cancelSave();
            isCollect = false;
        } else {
            try {
                if (mContent == null) {
                    mCollectSemaphore.acquire();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            saveFilm();
            isCollect = true;
        }
        supportInvalidateOptionsMenu();
    }

    /**
     * 用于保存filmContent和filmImage
     */
    private void saveFilm() {
        MyApplication.getDataSource().insertOrUpDataFilm(mId, mContent);
        if (mFile.exists()) {
            mFile.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(mFile);
            Bitmap bitmap = imageLoader.loadImageSync(mSubject.getImages().getLarge());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "收藏成功<(＾－＾)>！", Toast.LENGTH_SHORT).show();
    }

    private void cancelSave() {
        MyApplication.getDataSource().deleteFilm(mId);
        if (mFile.exists()) {
            mFile.delete();
        }
        Toast.makeText(this, "取消收藏︿(￣︶￣)︿！", Toast.LENGTH_SHORT).show();
    }

    /**
     * 启动新的activity
     */
    private void prepareIntent(Context context, Class cla, String id) {
        Intent intent = new Intent(context, cla);
        intent.putExtra("id", id);
        startActivity(intent);
        this.finish();
    }

    /**
     * 将List<String>转成合适的String
     */
    private String listToString(List<String> data) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < data.size(); i++) {
            s.append(i == 0 ? "" : "/");
            s.append(data.get(i));
        }
        return s.toString();
    }

    @Override
    public void onRefresh() {
        volley_GET();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        //利用AppBarLayout的回调接口启用或者关闭滑动刷新
        mRefresh.setEnabled(i == 0);
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
