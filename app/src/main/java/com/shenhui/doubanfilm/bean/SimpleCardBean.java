package com.shenhui.doubanfilm.bean;

public class SimpleCardBean {

    private String alt;
    private String id;
    private String name;
    private String image;
    private Boolean isDir = false;
    private Boolean isFilm = false;

    public SimpleCardBean() {
    }

    public SimpleCardBean(String alt, String id, String name, String image) {
        this.alt = alt;
        this.id = id;
        this.name = name;
        this.image = image;
        this.isFilm = true;
    }

    public SimpleCardBean(String alt, String id, String name, String medium, Boolean isDir) {
        this.alt = alt;
        this.id = id;
        this.name = name;
        this.image = medium;
        this.isDir = isDir;
        this.isFilm = false;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getIsDir() {
        return isDir;
    }

    public void setIsDir(Boolean isDir) {
        this.isDir = isDir;
    }

    public Boolean getIsFilm() {
        return isFilm;
    }

    public void setIsFilm(Boolean isFilm) {
        this.isFilm = isFilm;
    }
}
