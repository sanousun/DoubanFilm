package com.shenhui.doubanfilm.bean;

import java.util.List;

public class CelebrityBean {

    private String mobile_url;//条目移动版url
    private String name;//中文名
    private String name_en;//英文名
    private String gender;//性别
    private ImagesEntity avatars;//影人头像
    private String alt;//条目页url
    private String born_place;//出生地
    private String id;//条目id
    private List<String> aka_en;//更多英文名
    private List<String> aka;//更多中文名
    private List<WorksEntity> works;//影人作品,最多五部

    public void setMobile_url(String mobile_url) {
        this.mobile_url = mobile_url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAvatars(ImagesEntity avatars) {
        this.avatars = avatars;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public void setBorn_place(String born_place) {
        this.born_place = born_place;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAka_en(List<String> aka_en) {
        this.aka_en = aka_en;
    }

    public void setAka(List<String> aka) {
        this.aka = aka;
    }

    public void setWorks(List<WorksEntity> works) {
        this.works = works;
    }

    public String getMobile_url() {
        return mobile_url;
    }

    public String getName() {
        return name;
    }

    public String getName_en() {
        return name_en;
    }

    public String getGender() {
        return gender;
    }

    public ImagesEntity getAvatars() {
        return avatars;
    }

    public String getAlt() {
        return alt;
    }

    public String getBorn_place() {
        return born_place;
    }

    public String getId() {
        return id;
    }

    public List<String> getAka_en() {
        return aka_en;
    }

    public List<String> getAka() {
        return aka;
    }

    public List<WorksEntity> getWorks() {
        return works;
    }
}
