package com.cgy.colorfulnews.module.news.presenter.impl;

import com.cgy.colorfulnews.db.NewsChannel;
import com.cgy.colorfulnews.module.base.BasePresenterImpl;
import com.cgy.colorfulnews.module.news.interactor.NewsInteract;
import com.cgy.colorfulnews.module.news.interactor.impl.NewsInteractImpl;
import com.cgy.colorfulnews.module.news.presenter.NewsPresenter;
import com.cgy.colorfulnews.module.news.view.NewsView;

import java.util.List;

import javax.inject.Inject;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/11 17:13
 */
public class NewsPresenterImpl extends BasePresenterImpl<NewsView, List<NewsChannel>> implements NewsPresenter {

    private NewsInteract<List<NewsChannel>> mNewsInteract;

    @Inject
    public NewsPresenterImpl(NewsInteractImpl newsInteract) {
        mNewsInteract = newsInteract;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadNewsChannels();
    }

    private void loadNewsChannels() {
        mSubscription = mNewsInteract.loadNewsChannels(this);
    }

    @Override
    public void success(List<NewsChannel> data) {
        super.success(data);
        mView.initViewPager(data);
    }

    @Override
    public void onChannelDbChanged() {
        loadNewsChannels();
    }
}
