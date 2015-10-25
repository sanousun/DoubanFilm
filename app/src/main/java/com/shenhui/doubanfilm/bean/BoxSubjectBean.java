package com.shenhui.doubanfilm.bean;

import com.google.gson.annotations.SerializedName;

public class BoxSubjectBean {

    private int box;
    @SerializedName("new")
    private boolean newX;
    private int rank;
    private SimpleSubjectBean subject;

    public boolean isNewX() {
        return newX;
    }

    public void setBox(int box) {
        this.box = box;
    }

    public void setNewX(boolean newX) {
        this.newX = newX;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setSubject(SimpleSubjectBean subject) {
        this.subject = subject;
    }

    public int getBox() {
        return box;
    }

    public boolean getNewX() {
        return newX;
    }

    public int getRank() {
        return rank;
    }

    public SimpleSubjectBean getSubject() {
        return subject;
    }
}
