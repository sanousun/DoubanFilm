package com.shenhui.doubanfilm.support;

import com.google.gson.reflect.TypeToken;
import com.shenhui.doubanfilm.bean.Celebrity;
import com.shenhui.doubanfilm.bean.SimpleSub;
import com.shenhui.doubanfilm.bean.Subject;
import com.shenhui.doubanfilm.bean.USBoxSub;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 记录项目常量
 * <p/>
 * Created by sanousun on 2015/9/16.
 */
public class Constant {

    public static final String API = "http://api.douban.com";
    public static final String IN_THEATERS = "/v2/movie/in_theaters";
    public static final String US_BOX = "/v2/movie/us_box";
    public static final String COMING = "/v2/movie/coming_soon";
    public static final String TOP250 = "/v2/movie/top250";
    public static final String SUBJECT = "/v2/movie/subject/";
    public static final String CELEBRITY = "/v2/movie/celebrity/";
    public static final String SEARCH_Q = "/v2/movie/search?q=";
    public static final String SEARCH_TAG = "/v2/movie/search?tag=";

    public static final Type subType = new TypeToken<Subject>() {
    }.getType();
    public static final Type cleType = new TypeToken<Celebrity>() {
    }.getType();
    public static final Type simpleSubTypeList = new TypeToken<List<SimpleSub>>() {
    }.getType();
    public static final Type simpleBoxTypeList = new TypeToken<List<USBoxSub>>() {
    }.getType();
}
