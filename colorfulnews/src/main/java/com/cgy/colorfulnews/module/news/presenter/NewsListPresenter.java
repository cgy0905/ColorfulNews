package com.cgy.colorfulnews.module.news.presenter;

import com.cgy.colorfulnews.module.base.BasePresenter;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/12 15:21
 */
public interface NewsListPresenter extends BasePresenter {
    void setNewsTypeAndId(String newsType, String newsId);

    void refreshData();

    void loadMore();
}
