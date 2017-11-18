package com.shenhui.doubanfilm.data.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created with Android Studio.
 * User: dashu
 * Date: 2017/4/27
 * Time: 下午2:48
 * Desc: 影人条目信息
 */

public class CelebrityInfo {

    /**
     * 条目id
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
     * 英文名
     */
    @SerializedName("name_en")
    @Expose
    public String nameEn;
    /**
     * 条目页url
     */
    @SerializedName("alt")
    @Expose
    public String alt;
    /**
     * 条目页移动版url
     */
    @SerializedName("mobile_url")
    @Expose
    public String mobileUrl;
    /**
     * 影人头像，分别提供420px x 600px(大)，140px x 200px(中) 70px x 100px(小)尺寸
     */
    @SerializedName("avatars")
    @Expose
    public ImageInfo avatars;
//    /**
//     * 简介
//     */
//    @SerializedName("summary")
//    @Expose
//    public String summary;
    /**
     * 更多中文名
     */
    @SerializedName("aka")
    @Expose
    public List<String> aka;
    /**
     * 更多英文名
     */
    @SerializedName("aka_en")
    @Expose
    public List<String> akaEn;
//    /**
//     * 官方网站
//     */
//    @SerializedName("website")
//    @Expose
//    public String website;
    /**
     * 性别
     */
    @SerializedName("gender")
    @Expose
    public String gender;
//    /**
//     * 出生日期
//     */
//    @SerializedName("birthday")
//    @Expose
//    public String birthday;
    /**
     * 出生地
     */
    @SerializedName("born_place")
    @Expose
    public String bornPlace;
//    /**
//     * 职业
//     */
//    @SerializedName("professions")
//    @Expose
//    public List<String> professions;
//    /**
//     * 星座
//     */
//    @SerializedName("constellation")
//    @Expose
//    public String constellation;
//    /**
//     * 影人剧照，最多10张
//     */
//    @SerializedName("photos")
//    @Expose
//    public List<String> photos;
    /**
     * 影人作品，最多5部，简版电影条目结构
     */
    @SerializedName("works")
    @Expose
    public List<WorksInfo> works;
}
