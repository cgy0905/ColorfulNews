package com.cgy.colorfulnews.module.news.interactor;

import com.cgy.colorfulnews.listener.RequestCallback;

import rx.Subscription;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/13 18:23
 */
public interface NewsDetailInteract<T> {
    Subscription loadNewsDetail(RequestCallback<T> callback, String postId);
}
