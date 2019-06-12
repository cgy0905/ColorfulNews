package com.cgy.colorfulnews.module.news.presenter.impl;

import com.cgy.colorfulnews.entity.NewsSummary;
import com.cgy.colorfulnews.listener.RequestCallback;
import com.cgy.colorfulnews.module.base.BasePresenterImpl;
import com.cgy.colorfulnews.module.news.interactor.impl.NewsListInteractImpl;
import com.cgy.colorfulnews.module.news.interactor.NewsListInteract;
import com.cgy.colorfulnews.module.news.presenter.NewsListPresenter;
import com.cgy.colorfulnews.module.news.view.NewsListView;

import java.util.List;

import javax.inject.Inject;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/12 15:23
 */
public class NewsListPresenterImpl extends BasePresenterImpl<NewsListView, List<NewsSummary>>
        implements NewsListPresenter, RequestCallback<List<NewsSummary>> {


    private NewsListInteract<List<NewsSummary>> mNewsListInteractor;

    @Inject
    public NewsListPresenterImpl(NewsListInteractImpl newsListInteract) {
        mNewsListInteractor = newsListInteract;
    }

    @Override
    public void setNewsTypeAndId(String newsType, String newsId) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void loadMore() {

    }
}
