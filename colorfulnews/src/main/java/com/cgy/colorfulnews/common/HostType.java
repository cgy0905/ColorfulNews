package com.cgy.colorfulnews.common;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/12 14:35
 */
public class HostType {

    /**
     * 多少种Host类型
     */
    public static final int TYPE_COUNT = 3;

    /**
     * 网易新闻视频的host
     */
    public static final int NETEASE_NEWS_VIDEO = 1;

    /**
     * 新浪图片的host
     */
    public static final int GANK_GIRL_PHOTO = 2;

    /**
     * 新闻详情html图片的host
     */
    public static final int NEWS_DETAIL_HTML_PHOTO = 3;

    /**
     * 替代枚举的方案，使用IntDef保证类型安全
     */
    @IntDef({NETEASE_NEWS_VIDEO, GANK_GIRL_PHOTO, NEWS_DETAIL_HTML_PHOTO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HostTypeChecker {

    }
}
