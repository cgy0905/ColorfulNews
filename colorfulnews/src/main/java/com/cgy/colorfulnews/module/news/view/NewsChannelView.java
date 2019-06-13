package com.cgy.colorfulnews.module.news.view;

import com.cgy.colorfulnews.db.NewsChannel;
import com.cgy.colorfulnews.module.base.BaseView;

import java.util.List;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/13 13:52
 */
public interface NewsChannelView extends BaseView {
    void initRecyclerView(List<NewsChannel> newsChannelsMine, List<NewsChannel> newsChannelsMore);
}
