package com.shenhui.doubanfilm.base;

/**
 * @author dashu
 * @date 2018/3/19
 * desc: 加载列表的数据模型
 */

public class BasePullInfo {

    public static final int STATUS_LOADING = 0;
    public static final int STATUS_NO_MORE = 1;
    public static final int STATUS_ERROR = 2;

    private int status;
    private String desc;

    public BasePullInfo() {
        status = STATUS_LOADING;
        desc = "加载中...";
    }

    public void setStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isLoading() {
        return status == STATUS_LOADING;
    }

    public boolean isError() {
        return status == STATUS_ERROR;
    }
}
