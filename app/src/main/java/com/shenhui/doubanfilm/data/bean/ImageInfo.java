package com.shenhui.doubanfilm.data.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created with Android Studio.
 * User: dashu
 * Date: 2017/3/22
 * Time: 下午2:30
 * Desc: 图片信息
 */

public class ImageInfo {

    @SerializedName("small")
    @Expose
    public String small;

    @SerializedName("large")
    @Expose
    public String large;

    @SerializedName("medium")
    @Expose
    public String medium;
}
