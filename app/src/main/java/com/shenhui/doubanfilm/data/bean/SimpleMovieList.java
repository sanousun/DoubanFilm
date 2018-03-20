package com.shenhui.doubanfilm.data.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created with Android Studio.
 * User: dashu
 * Date: 2017/4/27
 * Time: 下午2:36
 * Desc: 简单的电影列表
 */

public class SimpleMovieList {
    /**
     * 起始位置
     */
    @SerializedName("start")
    @Expose
    public Integer start;
    /**
     * 条目数
     */
    @SerializedName("count")
    @Expose
    public Integer count;
    /**
     * 总数
     */
    @SerializedName("total")
    @Expose
    public Integer total;
    /**
     * 电影信息
     */
    @SerializedName("subjects")
    @Expose
    public List<SimpleMovieInfo> subjects;
}
