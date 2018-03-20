package com.shenhui.doubanfilm.bean;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "subject")
public class SubjectBean {

    private RatingEntity rating;//评分
    private int reviews_count;//影评数量
    private int wish_count;//想看人数
    private int collect_count;//看过人数
    private String douban_site;//豆瓣小站
    private String year;//年代
    private ImagesEntity images;//电影海报图，分别提供288px x 465px(大)，96px x 155px(中) 64px x 103px(小)尺寸
    private String alt;//条目页URL
    @PrimaryKey
    private String id;//条目id
    private String mobile_url;//移动版条目页URL
    private String title;//中文名
    private Object do_count;//在看人数，如果是电视剧，默认值为0，如果是电影值为null
    private Object seasons_count;//总季数(tv only)
    private String schedule_url;//影讯页URL(movie only)
    private Object episodes_count;//当前季的集数(tv only)
    private Object current_season;//当前季数(tv only)
    private String original_title;//原名
    private String summary;//简介
    private String subtype;//条目分类, movie或者tv
    private int comments_count;//短评数量
    private int ratings_count;//评分人数
    private List<String> genres;//影片类型，最多提供3个
    private List<String> countries;//制片国家/地区
    private List<CelebrityEntity> casts;//主演，最多可获得4个，数据结构为影人的简化描述
    private List<CelebrityEntity> directors;//导演，数据结构为影人的简化描述
    private List<String> aka;//又名

    public String getLocalImageFile() {
        return localImageFile;
    }

    public void setLocalImageFile(String localImageFile) {
        this.localImageFile = localImageFile;
    }

    private String localImageFile;

    public void setRating(RatingEntity rating) {
        this.rating = rating;
    }

    public void setReviews_count(int reviews_count) {
        this.reviews_count = reviews_count;
    }

    public void setWish_count(int wish_count) {
        this.wish_count = wish_count;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    public void setDouban_site(String douban_site) {
        this.douban_site = douban_site;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setImages(ImagesEntity images) {
        this.images = images;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMobile_url(String mobile_url) {
        this.mobile_url = mobile_url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDo_count(Object do_count) {
        this.do_count = do_count;
    }

    public void setSeasons_count(Object seasons_count) {
        this.seasons_count = seasons_count;
    }

    public void setSchedule_url(String schedule_url) {
        this.schedule_url = schedule_url;
    }

    public void setEpisodes_count(Object episodes_count) {
        this.episodes_count = episodes_count;
    }

    public void setCurrent_season(Object current_season) {
        this.current_season = current_season;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public void setRatings_count(int ratings_count) {
        this.ratings_count = ratings_count;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public void setCasts(List<CelebrityEntity> casts) {
        this.casts = casts;
    }

    public void setDirectors(List<CelebrityEntity> directors) {
        this.directors = directors;
    }

    public void setAka(List<String> aka) {
        this.aka = aka;
    }

    public RatingEntity getRating() {
        return rating;
    }

    public int getReviews_count() {
        return reviews_count;
    }

    public int getWish_count() {
        return wish_count;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public String getDouban_site() {
        return douban_site;
    }

    public String getYear() {
        return year;
    }

    public ImagesEntity getImages() {
        return images;
    }

    public String getAlt() {
        return alt;
    }

    public String getId() {
        return id;
    }

    public String getMobile_url() {
        return mobile_url;
    }

    public String getTitle() {
        return title;
    }

    public Object getDo_count() {
        return do_count;
    }

    public Object getSeasons_count() {
        return seasons_count;
    }

    public String getSchedule_url() {
        return schedule_url;
    }

    public Object getEpisodes_count() {
        return episodes_count;
    }

    public Object getCurrent_season() {
        return current_season;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getSummary() {
        return summary;
    }

    public String getSubtype() {
        return subtype;
    }

    public int getComments_count() {
        return comments_count;
    }

    public int getRatings_count() {
        return ratings_count;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<String> getCountries() {
        return countries;
    }

    public List<CelebrityEntity> getCasts() {
        return casts;
    }

    public List<CelebrityEntity> getDirectors() {
        return directors;
    }

    public List<String> getAka() {
        return aka;
    }
}
