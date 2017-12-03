package com.shenhui.doubanfilm.data.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created with Android Studio.
 * User: dashu
 * Date: 2017/3/22
 * Time: 下午2:33
 * Desc: 评分信息
 */

public class RateInfo {
    /**
     * 最低评分
     */
    @SerializedName("min")
    @Expose
    public Integer min;
    /**
     * 最高评分
     */
    @SerializedName("max")
    @Expose
    public Integer max;
    /**
     * 评分
     */
    @SerializedName("average")
    @Expose
    public Float average;
    /**
     * 关注
     */
    @SerializedName("stars")
    @Expose
    public String stars;
}
