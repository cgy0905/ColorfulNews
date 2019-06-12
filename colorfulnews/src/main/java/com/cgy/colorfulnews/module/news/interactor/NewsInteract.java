package com.cgy.colorfulnews.module.news.interactor;

import com.cgy.colorfulnews.listener.RequestCallback;

import rx.Subscription;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/12 14:22
 */
public interface NewsInteract<T> {
    Subscription loadNewsChannels(RequestCallback<T> callback);
}
