package com.shenhui.doubanfilm.data.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created with Android Studio.
 * User: dashu
 * Date: 2017/3/22
 * Time: 下午2:40
 * Desc: 简单的影人信息
 */

public class SimpleCelebrityInfo {
    /**
     * 影人条目id
     */
    @SerializedName("id")
    @Expose
    public String id;
    /**
     * 中文名
     */
    @SerializedName("name")
    @Expose
    public String name;
    /**
     * 影人条目URL
     */
    @SerializedName("alt")
    @Expose
    public String alt;
    /**
     * 影人头像，分别提供420px x 600px(大)，140px x 200px(中) 70px x 100px(小)尺寸
     */
    @SerializedName("avatars")
    @Expose
    public ImageInfo avatars;
}
