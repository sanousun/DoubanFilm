package com.shenhui.doubanfilm.data.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created with Android Studio.
 * User: dashu
 * Date: 2017/3/22
 * Time: 下午2:30
 * Desc: 电影条目信息
 */

public class MovieInfo {
    /**
     * 条目id
     */
    @SerializedName("id")
    @Expose
    public String id;
    /**
     * 中文名
     */
    @SerializedName("title")
    @Expose
    public String title;
    /**
     * 原名
     */
    @SerializedName("original_title")
    @Expose
    public String originalTitle;
    /**
     * 又名
     */
    @SerializedName("aka")
    @Expose
    public List<String> aka;
    /**
     * 条目页URL
     */
    @SerializedName("alt")
    @Expose
    public String alt;
    /**
     * 移动版条目页URL
     */
    @SerializedName("mobile_url")
    @Expose
    public String mobileUrl;
    /**
     * 评分信息
     */
    @SerializedName("rating")
    @Expose
    public RateInfo rating;
    /**
     * 评分人数
     */
    @SerializedName("ratings_count")
    @Expose
    public Integer ratingsCount;
    /**
     * 想看人数
     */
    @SerializedName("wish_count")
    @Expose
    public Integer wishCount;
    /**
     * 看过人数
     */
    @SerializedName("collect_count")
    @Expose
    public Integer collectCount;
    /**
     * 在看人数，如果是电视剧，默认值为0，如果是电影值为null
     */
    @SerializedName("do_count")
    @Expose
    public Integer doCount;
    /**
     * 电影海报图，分别提供288px x 465px(大)，96px x 155px(中) 64px x 103px(小)尺寸
     */
    @SerializedName("images")
    @Expose
    public ImageInfo images;
    /**
     * 条目分类, movie或者tv
     */
    @SerializedName("subtype")
    @Expose
    public String subtype;
    /**
     * 导演
     */
    @SerializedName("directors")
    @Expose
    public List<SimpleCelebrityInfo> directors;
    /**
     * 主演，最多可获得4个
     */
    @SerializedName("casts")
    @Expose
    public List<SimpleCelebrityInfo> casts;
    /**
     * 年代
     */
    @SerializedName("year")
    @Expose
    public String year;
    /**
     * 影片类型，最多提供3个
     */
    @SerializedName("genres")
    @Expose
    public List<String> genres;
    /**
     * 制片国家/地区
     */
    @SerializedName("countries")
    @Expose
    public List<String> countries;
    /**
     * 简介
     */
    @SerializedName("summary")
    @Expose
    public String summary;
}
