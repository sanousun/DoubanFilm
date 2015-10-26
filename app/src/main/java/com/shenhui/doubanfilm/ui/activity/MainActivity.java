package com.shenhui.doubanfilm.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;
import android.widget.Toast;

import com.shenhui.doubanfilm.R;
import com.shenhui.doubanfilm.base.BaseFragment;
import com.shenhui.doubanfilm.ui.fragment.CollectFragment;
import com.shenhui.doubanfilm.ui.fragment.HomeFragment;
import com.shenhui.doubanfilm.ui.fragment.TopFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

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

    private CircleImageView mNavImage;
    private CircleImageView mNavEdit;
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
    private SharedPreferences.Editor useEditor;

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
        mNavImage = (CircleImageView) mNavHeader.findViewById(R.id.iv_nav_header);
        mNavEdit = (CircleImageView) mNavHeader.findViewById(R.id.iv_nav_edit);
        mUserName = (TextView) mNavHeader.findViewById(R.id.tv_nav_name);
        mUserIntro = (TextView) mNavHeader.findViewById(R.id.tv_nav_intro);
    }

    protected void initData() {
        mTitle = getString(R.string.nav_home);
        mToolbar.setTitle(mTitle);
        setSupportActionBar(mToolbar);
        //设置Drawer的开关
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.openDrawer, R.string.closeDrawer);
        mToggle.syncState();
        mDrawer.setDrawerListener(mToggle);
        //初始化Viewpager
        mFragmentManager = getSupportFragmentManager();
        mCurFragment = mFragmentManager.findFragmentByTag(mTitle);
        if (mCurFragment == null) {
            Fragment homeFragment = new HomeFragment();
            mFragmentManager.beginTransaction().add(R.id.main_container,
                    homeFragment, mTitle).commit();
            mCurFragment = homeFragment;
        }

        mFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                PICTURE_HEADER_FILE);
        if (mFile.exists()) {
            Bitmap header = BitmapFactory.decodeFile(mFile.getPath());
            mNavImage.setImageBitmap(header);
        } else {
            mNavImage.setImageResource(R.drawable.lks_for_blank_url);
        }
        mNavView.getMenu().getItem(0).setChecked(true);
        userSP = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        useEditor = userSP.edit();
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
            case R.id.iv_nav_edit:
                View v = LayoutInflater.from(this).
                        inflate(R.layout.dialog_user_edit, null);
                final EditText nameEdit = (EditText) v.findViewById(R.id.edit_user_name);
                final EditText introEdit = (EditText) v.findViewById(R.id.edit_user_intro);
                new AlertDialog.Builder(this).setTitle(getString(R.string.user_edit)).
                        setView(v).
                        setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String name = nameEdit.getText().toString().trim();
                                String intro = introEdit.getText().toString().trim();
                                if (name.equals("") && intro.equals("")) {
                                    dialogInterface.cancel();
                                    return;
                                }
                                if (!name.equals("")) {
                                    useEditor.putString(USER_NAME, name);
                                    mUserName.setText(name);
                                }
                                if (!intro.equals("")) {
                                    useEditor.putString(USER_INTRO, intro);
                                    mUserIntro.setText(intro);
                                }
                                useEditor.commit();
                                dialogInterface.cancel();
                            }
                        }).show();
                break;
            case R.id.iv_nav_header:
                new AlertDialog.Builder(this).
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
        menuItem.setChecked(true);
        mDrawer.closeDrawers();
        changeFragment(menuItem.getTitle().toString());
        return true;
    }

    /**
     * 判断各种逻辑下的fragment显示问题
     */
    private void changeFragment(String title) {
        mTitle = title;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.hide(mCurFragment);
        Fragment fragment = mFragmentManager.findFragmentByTag(title);
        if (fragment == null) {
            fragment = newFragment(title);
            transaction.add(R.id.main_container, fragment, title);
            mCurFragment = fragment;
        } else {
            transaction.show(fragment);
            mCurFragment = fragment;
        }
        transaction.commit();
        supportInvalidateOptionsMenu();
        if (mTitle != null) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) actionBar.setTitle(mTitle);
        }
    }

    /**
     * 根据menuItem的title返回对应的fragment
     */
    private Fragment newFragment(String title) {
        switch (title) {
            case "首页":
                return new HomeFragment();
            case "收藏":
                return new CollectFragment();
            case "豆瓣top250":
                return new TopFragment();
        }
        return new BaseFragment();
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
                changeFragment(getString(R.string.nav_home));
                homeItem.setChecked(true);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem setting = menu.findItem(R.id.action_settings);
        if (mTitle.equals(getString(R.string.nav_home))) {
            setting.setVisible(true);
        } else {
            setting.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                prepareIntent(SearchActivity.class);
                break;
            case R.id.action_settings:
                prepareIntent(PrefsActivity.class);
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
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
                break;
            case 1:
                Intent cameraIntent = new Intent(
                        "android.media.action.IMAGE_CAPTURE");
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
            mNavImage.setImageBitmap(photo);
            saveBitmap(photo);
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
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
