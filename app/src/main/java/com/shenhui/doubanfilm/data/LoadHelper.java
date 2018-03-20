package com.shenhui.doubanfilm.data;

import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author dashu
 * @date 2018/3/16
 * 用来添加一些共用流程
 */

public class LoadHelper {

    public static <T> FlowableTransformer<T, T> transformer() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
