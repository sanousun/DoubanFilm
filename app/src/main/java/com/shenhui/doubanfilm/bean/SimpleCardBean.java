package com.shenhui.doubanfilm.bean;

public class SimpleCardBean {

    private String id;
    private String name;
    private String image;
    private Boolean isFilm;

    public SimpleCardBean() {

    }

    public SimpleCardBean(String id, String name, String medium, Boolean isFilm) {
        this.id = id;
        this.name = name;
        this.image = medium;
        this.isFilm = isFilm;
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

    public Boolean getIsFilm() {
        return isFilm;
    }

    public void setIsFilm(Boolean isFilm) {
        this.isFilm = isFilm;
    }
}
