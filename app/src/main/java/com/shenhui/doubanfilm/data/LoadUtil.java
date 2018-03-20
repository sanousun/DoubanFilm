package com.shenhui.doubanfilm.data;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author dashu
 * @date 2018/3/15
 * 接口访问器
 */

public class LoadUtil {

    private static final String BASE_URL = "http://api.douban.com/";

    private static LoadUtil sLoadUtil;
    private LoadService mLoadService;

    private LoadUtil() {
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new )
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        mLoadService = retrofit.create(LoadService.class);
    }

    private static LoadUtil getInstance() {
        if (sLoadUtil == null) {
            synchronized (LoadUtil.class) {
                if (sLoadUtil == null) {
                    sLoadUtil = new LoadUtil();
                }
            }
        }
        return sLoadUtil;
    }

    /**
     * 获取接口服务
     *
     * @return 接口
     */
    public static LoadService getService() {
        return LoadUtil.getInstance().mLoadService;
    }
}
