package com.shenhui.doubanfilm.bean;

/**
 * Created by sanousun on 2015/9/4.
 */
public class CastAndCommend extends Subject.CastsEntity {

    private String alt;
    private String id;
    private String name;
    private String image;
    private Boolean isDir = false;
    private Boolean isCom = false;

    public CastAndCommend(String alt, String id, String name, String image) {
        this.alt = alt;
        this.id = id;
        this.name = name;
        this.image = image;
        this.isCom = true;
    }

    public CastAndCommend(String alt, String id, String name, String medium, Boolean isDir) {
        this.alt = alt;
        this.id = id;
        this.name = name;
        this.image = medium;
        this.isDir = isDir;
        this.isCom = false;
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

    public String getMedium() {
        return image;
    }

    public void setMedium(String medium) {
        this.image = medium;
    }

    public Boolean getIsDir() {
        return isDir;
    }

    public void setIsDir(Boolean isDir) {
        this.isDir = isDir;
    }

    public Boolean getIsCom() {
        return isCom;
    }

    public void setIsCom(Boolean isCom) {
        this.isCom = isCom;
    }
}
