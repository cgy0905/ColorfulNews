package com.cgy.colorfulnews.module.news.interactor;

import com.cgy.colorfulnews.listener.RequestCallback;

import rx.Subscription;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/12 15:26
 */
public interface NewsListInteract<T> {

    Subscription loadNews(RequestCallback<T> listener, String type, String id, int startPage);
}
