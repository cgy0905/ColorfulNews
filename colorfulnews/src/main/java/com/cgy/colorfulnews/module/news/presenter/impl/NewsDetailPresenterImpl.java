package com.cgy.colorfulnews.module.news.presenter.impl;

import com.cgy.colorfulnews.entity.NewsDetail;
import com.cgy.colorfulnews.module.base.BasePresenterImpl;
import com.cgy.colorfulnews.module.news.interactor.NewsDetailInteract;
import com.cgy.colorfulnews.module.news.interactor.impl.NewsDetailInteractImpl;
import com.cgy.colorfulnews.module.news.presenter.NewsDetailPresenter;
import com.cgy.colorfulnews.module.news.view.NewsDetailView;

import javax.inject.Inject;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/13 18:21
 */
public class NewsDetailPresenterImpl extends BasePresenterImpl<NewsDetailView, NewsDetail> implements NewsDetailPresenter {

    private NewsDetailInteract<NewsDetail> mNewsDetailInteract;
    private String mPostId;

    @Inject
    public NewsDetailPresenterImpl(NewsDetailInteractImpl newsDetailInteract) {
        mNewsDetailInteract = newsDetailInteract;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSubscription = mNewsDetailInteract.loadNewsDetail(this, mPostId);
    }

    @Override
    public void success(NewsDetail data) {
        super.success(data);
        mView.setNewsDetail(data);
    }

    @Override
    public void setPostId(String postId) {
        mPostId = postId;
    }
}
