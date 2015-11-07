package com.shenhui.doubanfilm.bean;

import java.util.List;

public class SimpleSubjectBean {

    private RatingEntity rating;
    private int collect_count;
    private String title;
    private String original_title;
    private String subtype;
    private String year;
    private ImagesEntity images;
    private String alt;
    private String id;
    private List<String> genres;
    private List<CelebrityEntity> casts;
    private List<CelebrityEntity> directors;

    public void setRating(RatingEntity rating) {
        this.rating = rating;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
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

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setCasts(List<CelebrityEntity> casts) {
        this.casts = casts;
    }

    public void setDirectors(List<CelebrityEntity> directors) {
        this.directors = directors;
    }

    public RatingEntity getRating() {
        return rating;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getSubtype() {
        return subtype;
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

    public List<String> getGenres() {
        return genres;
    }

    public List<CelebrityEntity> getCasts() {
        return casts;
    }

    public List<CelebrityEntity> getDirectors() {
        return directors;
    }
}
