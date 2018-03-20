package com.shenhui.doubanfilm.data;

import com.shenhui.doubanfilm.data.bean.CelebrityInfo;
import com.shenhui.doubanfilm.data.bean.MovieInfo;
import com.shenhui.doubanfilm.data.bean.SimpleBoxMovieInfo;
import com.shenhui.doubanfilm.data.bean.SimpleMovieInfo;
import com.shenhui.doubanfilm.data.bean.SimpleMovieList;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author dashu
 * @date 2017/3/22
 * retrofit的接口定义
 */

public interface LoadService {

    /**
     * 获取上映电影
     */
    @GET("/v2/movie/in_theaters")
    Flowable<SimpleMovieList> getMovieInTheaters(@Query("start") int start);

    /**
     * 获取即将上映电影
     */
    @GET("/v2/movie/coming_soon")
    Flowable<List<SimpleMovieInfo>> getMovieComingSoon(@Query("start") int start);

    /**
     * 获取北美电影票房排行榜
     */
    @GET("/v2/movie/us_box")
    Flowable<List<SimpleBoxMovieInfo>> getMovieForUSBox();

    /**
     * 获取top250电影信息
     */
    @GET("/v2/movie/top250")
    Flowable<List<SimpleBoxMovieInfo>> getMovieTop250(@Query("start") int start);

    /**
     * 获取电影详情
     */
    @GET("/v2/movie/subject/{id}")
    Flowable<MovieInfo> getMovieDetail(@Path("id") String id);

    /**
     * 获取影人详情
     */
    @GET("/v2/movie/celebrity/{id}")
    Flowable<CelebrityInfo> getCelebrityDetail(@Path("id") String id);

    /**
     * 电影搜索
     */
    @GET("/v2/movie/search")
    Flowable<List<SimpleMovieInfo>> getSearchResult(@Query("q") String q, @Query("tag") String tag);
}
