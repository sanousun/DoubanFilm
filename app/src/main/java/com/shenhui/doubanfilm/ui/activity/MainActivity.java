package com.shenhui.doubanfilm.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.app.GlideApp;
import com.shenhui.doubanfilm.ui.fragment.BaseFragment;
import com.shenhui.doubanfilm.ui.fragment.CollectFragment;
import com.shenhui.doubanfilm.ui.fragment.HomeFragment;
import com.shenhui.doubanfilm.ui.fragment.TopFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DialogInterface.OnClickListener, View.OnClickListener {

    //for changing headerImage
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESIZE_REQUEST_CODE = 2;

    //for user information
    private static final String USER_INFO = "user information";
    private static final String USER_NAME = "user name";
    private static final String USER_INTRO = "user introduction";

    //the file for headerImage
    private static final String PICTURE_HEADER_FILE = "header.jpg";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.main_nav)
    NavigationView mNavView;
    @Bind(R.id.main_drawer)
    DrawerLayout mDrawer;

    private ImageView mNavImage;
    private ImageView mNavEdit;
    private TextView mUserName;
    private TextView mUserIntro;
    private MenuItem homeItem;
    private File mFile;

    private FragmentManager mFragmentManager;
    private Fragment mCurFragment;

    private String mTitle;

    /**
     * 记录系统时间，用于退出时做判断
     */
    private long exitTime = 0;

    private SharedPreferences userSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    protected void initView() {
        homeItem = mNavView.getMenu().findItem(R.id.nav_home);
        View mNavHeader = mNavView.inflateHeaderView(R.layout.view_nav_header);
        mNavImage = mNavHeader.findViewById(R.id.iv_view_nav_header);
        mNavEdit = mNavHeader.findViewById(R.id.iv_view_nav_edit);
        mUserName = mNavHeader.findViewById(R.id.tv_view_nav_name);
        mUserIntro = mNavHeader.findViewById(R.id.tv_view_nav_intro);
    }

    protected void initData() {
        mTitle = getString(R.string.nav_home);
        mToolbar.setTitle(mTitle);
        setSupportActionBar(mToolbar);
        //设置Drawer的开关
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.openDrawer, R.string.closeDrawer);
        mToggle.syncState();
        mDrawer.addDrawerListener(mToggle);
        //初始化Viewpager
        mFragmentManager = getSupportFragmentManager();
        mCurFragment = mFragmentManager.findFragmentByTag(mTitle);
        if (mCurFragment == null) {
            Fragment homeFragment = new HomeFragment();
            mFragmentManager.beginTransaction().
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                    add(R.id.rl_main_container, homeFragment, mTitle).commit();
            mCurFragment = homeFragment;
        }

        //头像设置
        mFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                PICTURE_HEADER_FILE);
        if (mFile.exists()) {
            GlideApp.with(this)
                    .load(mFile)
                    .circleCrop()
                    .into(mNavImage);
        } else {
            GlideApp.with(this)
                    .load(R.drawable.def_header)
                    .circleCrop()
                    .into(mNavImage);
        }
        mNavView.setItemIconTintList(null);
        userSP = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        mUserName.setText(userSP.getString(USER_NAME, getString(R.string.user_name)));
        mUserIntro.setText(userSP.getString(USER_INTRO, getString(R.string.user_introduction)));
    }

    protected void initEvent() {
        mNavView.setNavigationItemSelectedListener(this);
        mNavEdit.setOnClickListener(this);
        mNavImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_view_nav_edit:
                View v = LayoutInflater.from(MainActivity.this).
                        inflate(R.layout.dialog_user_edit, null);
                final EditText nameEdit = (EditText) v.findViewById(R.id.edit_dialog_user_name);
                final EditText introEdit = (EditText) v.findViewById(R.id.edit_dialog_user_intro);
                new AlertDialog.Builder(MainActivity.this).setTitle(getString(R.string.user_edit)).
                        setView(v).
                        setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Editor editor = userSP.edit();
                                String name = nameEdit.getText().toString().trim();
                                String intro = introEdit.getText().toString().trim();
                                if (name.equals("") && intro.equals("")) {
                                    dialogInterface.cancel();
                                    return;
                                }
                                if (!name.equals("")) {
                                    editor.putString(USER_NAME, name);
                                    mUserName.setText(name);
                                }
                                if (!intro.equals("")) {
                                    editor.putString(USER_INTRO, intro);
                                    mUserIntro.setText(intro);
                                }
                                editor.apply();
                                dialogInterface.cancel();
                            }
                        }).show();
                break;
            case R.id.iv_view_nav_header:
                new AlertDialog.Builder(MainActivity.this).
                        setTitle(getString(R.string.select_header)).
                        setItems(getResources().getStringArray(R.array.select_header_item),
                                this).show();
                break;
        }
    }

    /**
     * NavigationView的item点击事件处理
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_setting) {
            prepareIntent(PrefsActivity.class);
            return true;
        }
        menuItem.setChecked(true);
        mDrawer.closeDrawers();
        switchFragment(menuItem.getTitle().toString());
        return true;
    }

    /**
     * 判断各种逻辑下的fragment显示问题
     */
    private void switchFragment(String title) {
        mTitle = title;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(title);
        if (fragment == null) {
            transaction.hide(mCurFragment);
            fragment = createFragmentByTitle(title);
            transaction.add(R.id.rl_main_container, fragment, title);
            mCurFragment = fragment;
        } else if (fragment != mCurFragment) {
            transaction.hide(mCurFragment).show(fragment);
            mCurFragment = fragment;
        }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                commit();
        supportInvalidateOptionsMenu();
        if (mTitle != null) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) actionBar.setTitle(mTitle);
        }
    }

    /**
     * 根据menuItem的title返回对应的fragment
     */
    private Fragment createFragmentByTitle(String title) {
        switch (title) {
            case "首页":
                return new HomeFragment();
            case "收藏":
                return new CollectFragment();
            case "豆瓣top250":
                return new TopFragment();
            default:
                return new BaseFragment();
        }
    }

    /**
     * 处理返回键逻辑或者使用onBackPressed()
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mCurFragment instanceof HomeFragment) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    this.finish();
                    System.exit(0);
                }
            } else {
                switchFragment(getString(R.string.nav_home));
                homeItem.setChecked(true);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                prepareIntent(SearchActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepareIntent(Class cla) {
        this.startActivity(new Intent(MainActivity.this, cla));
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case 0:
                //打开系统图库
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
                break;
            case 1:
                //打开系统相机
                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
                cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                break;
        }
        dialogInterface.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    resizeImage(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    resizeImage(getImageUri());
                    break;
                case RESIZE_REQUEST_CODE:
                    if (data != null) {
                        showResizeImage(data);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 显示得到的经裁剪后的头像并保存
     */
    private void showResizeImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            GlideApp.with(this)
                    .load(photo)
                    .circleCrop()
                    .into(mNavImage);
        }
    }

    /**
     * 裁剪获得的图片
     */
    private void resizeImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESIZE_REQUEST_CODE);
    }

    public Uri getImageUri() {
        return Uri.fromFile(new File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                PICTURE_HEADER_FILE));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 将得到头像图片保存到手机中
     */
    private void saveBitmap(Bitmap bitmap) {
        if (mFile.exists()) {
            mFile.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(mFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
