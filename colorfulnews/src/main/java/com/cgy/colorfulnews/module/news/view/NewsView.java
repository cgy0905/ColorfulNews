package com.cgy.colorfulnews.module.news;

import com.cgy.colorfulnews.db.NewsChannel;
import com.cgy.colorfulnews.module.base.BaseView;

import java.util.List;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/11 17:07
 */
public interface NewsView extends BaseView {

    void initViewPager(List<NewsChannel> newsChannels);
}
