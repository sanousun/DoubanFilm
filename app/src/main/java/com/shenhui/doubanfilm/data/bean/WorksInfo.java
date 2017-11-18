package com.shenhui.doubanfilm.data.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created with Android Studio.
 * <p>
 * User dashu
 * date: 2017/11/17
 * time: 下午12:00
 * desc: 影人作品
 */

public class WorksInfo {
    /**
     * 角色
     */
    @SerializedName("roles")
    @Expose
    public List<String> roles;
    /**
     * 电影信息
     */
    @SerializedName("subject")
    @Expose
    public SimpleMovieInfo subject;
}
