package com.shenhui.doubanfilm.data.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created with Android Studio.
 * User: dashu
 * Date: 2017/4/27
 * Time: 下午2:36
 * Desc: 简单的电影信息
 */

public class SimpleMovieInfo {
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
     * 年代
     */
    @SerializedName("year")
    @Expose
    public String year;
    /**
     * 评分信息
     */
    @SerializedName("rating")
    @Expose
    public RateInfo rating;
    /**
     * 看过人数
     */
    @SerializedName("collect_count")
    @Expose
    public Integer collectCount;
    /**
     * 条目页URL
     */
    @SerializedName("alt")
    @Expose
    public String alt;
    /**
     * 影片类型，最多提供3个
     */
    @SerializedName("genres")
    @Expose
    public List<String> genres;

    public String getGenreStr() {
        if (genres == null || genres.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String genre : genres) {
            sb.append(genre).append(" ");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 导演
     */
    @SerializedName("directors")
    @Expose
    public List<SimpleCelebrityInfo> directors;

    public String getDirectorStr() {
        if (directors == null || directors.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (SimpleCelebrityInfo celebrityInfo : directors) {
            sb.append(celebrityInfo.name).append(" ");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 主演，最多可获得4个
     */
    @SerializedName("casts")
    @Expose
    public List<SimpleCelebrityInfo> casts;

    public String getCastStr() {
        if (casts == null || casts.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (SimpleCelebrityInfo celebrityInfo : casts) {
            sb.append(celebrityInfo.name).append(" ");
        }
        return sb.substring(0, sb.length() - 1);
    }
}
