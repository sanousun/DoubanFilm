package com.shenhui.doubanfilm.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sanousun on 2015/9/8.
 */
public class USBoxSub {

    private int box;
    @SerializedName("new")
    private boolean newX;
    private int rank;
    private SimpleSub subject;

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

    public void setSubject(SimpleSub subject) {
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

    public SimpleSub getSubject() {
        return subject;
    }
}
