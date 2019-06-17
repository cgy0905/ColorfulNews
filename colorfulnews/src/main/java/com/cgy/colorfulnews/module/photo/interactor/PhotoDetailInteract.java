package com.cgy.colorfulnews.module.photo.interactor;

import com.cgy.colorfulnews.listener.RequestCallback;

import rx.Subscription;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/17 11:22
 */
public interface PhotoDetailInteract<T> {
    Subscription saveImageAndGetImageUri(RequestCallback<T> listener, String url);
}
