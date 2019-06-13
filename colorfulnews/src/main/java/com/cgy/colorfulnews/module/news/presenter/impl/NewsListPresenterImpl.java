package com.cgy.colorfulnews.module.news.presenter.impl;

import com.cgy.colorfulnews.common.LoadNewsType;
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


    private NewsListInteract<List<NewsSummary>> mNewsListInteract;
    private String mNewsType;
    private String mNewsId;
    private int mStartPage;
    private boolean mIsFirstLoad;
    private boolean mIsRefresh = true;

    @Inject
    public NewsListPresenterImpl(NewsListInteractImpl newsListInteract) {
        mNewsListInteract = newsListInteract;
    }

    @Override
    public void onCreate() {
        if (mView != null) {
            loadNewsData();
        }
    }

    @Override
    public void beforeRequest() {
        if (!mIsFirstLoad) {
            mView.showProgress();
        }
    }

    @Override
    public void onError(String errorMsg) {
        super.onError(errorMsg);
        if (mView != null) {
            int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_ERROR : LoadNewsType.TYPE_LOAD_MORE_ERROR;
            mView.setNewsList(null, loadType);
        }
    }

    @Override
    public void success(List<NewsSummary> data) {
        mIsFirstLoad = true;
        if (data != null) {
            mStartPage += 20;
        }

        int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_SUCCESS : LoadNewsType.TYPE_LOAD_MORE_SUCCESS;
        if (mView != null) {
            mView.setNewsList(data, loadType);
            mView.hideProgress();
        }
    }

    @Override
    public void setNewsTypeAndId(String newsType, String newsId) {
        mNewsType = newsType;
        mNewsId = newsId;
    }

    @Override
    public void refreshData() {
        mStartPage = 0;
        mIsRefresh = true;
        loadNewsData();
    }

    @Override
    public void loadMore() {
        mIsRefresh = false;
        loadNewsData();
    }

    private void loadNewsData() {
        mSubscription = mNewsListInteract.loadNews(this, mNewsType, mNewsId, mStartPage);
    }
}
