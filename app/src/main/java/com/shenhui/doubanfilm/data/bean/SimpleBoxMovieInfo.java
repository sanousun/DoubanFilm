package com.shenhui.doubanfilm.data.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created with Android Studio.
 * <p>
 * User: dashu
 * date: 2017/11/17
 * time: 上午11:48
 * desc: 票房榜电影信息
 */

public class SimpleBoxMovieInfo {
    /**
     * 排名
     */
    @SerializedName("rank")
    @Expose
    public Integer rank;
    /**
     * 票房
     */
    @SerializedName("box")
    @Expose
    public Integer box;
    /**
     * 是否新上映
     */
    @SerializedName("new")
    @Expose
    public Integer isNew;
    /**
     * 电影信息
     */
    @SerializedName("subject")
    @Expose
    public SimpleMovieInfo subject;
}
