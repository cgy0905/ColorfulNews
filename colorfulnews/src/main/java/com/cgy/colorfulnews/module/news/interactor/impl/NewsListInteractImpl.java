package com.cgy.colorfulnews.module.news.interactor;

import com.cgy.colorfulnews.entity.NewsSummary;
import com.cgy.colorfulnews.listener.RequestCallback;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/12 15:27
 */
public class NewsListInteractImpl implements NewsListInteractor<List<NewsSummary>>{

    @Inject
    public NewsListInteractImpl(){}

    @Override
    public Subscription loadNews(RequestCallback<List<NewsSummary>> listener, String type, String id, int startPage) {
        // mIsNetError = false;
        // 对API调用了observeOn(MainThread)之后，线程会跑在主线程上，包括onComplete也是，
        // unsubscribe也在主线程，然后如果这时候调用call.cancel会导致NetworkOnMainThreadException
        // 加一句unsubscribeOn(io)
        return null;
    }
}
