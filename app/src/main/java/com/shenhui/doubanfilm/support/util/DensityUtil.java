package com.shenhui.doubanfilm.support.util;

import android.content.Context;

/**
 * dp和px之间的互相转化
 */
public class DensityUtil {

    /**
     * 根据手机的分辨率从 dp 的单位转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
