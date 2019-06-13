package com.cgy.colorfulnews.repository.net;

import com.cgy.colorfulnews.entity.NewsDetail;
import com.cgy.colorfulnews.entity.NewsSummary;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/12 15:31
 */
public interface NewsService {

    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Map<String, List<NewsSummary>>> getNewsList(
            @Header("Cache-Control") String cacheControl,
            @Path("type") String type, @Path("id") String id,
            @Path("startPage") int startPage);

    Observable<Map<String, NewsDetail>> getNewsDetail(
            @Header("Cache-Control") String cacheControl,
            @Path("postId") String postId);

}
