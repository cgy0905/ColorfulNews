package com.cgy.colorfulnews.module.photo.interactor;

import com.cgy.colorfulnews.listener.RequestCallback;

import rx.Subscription;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/14 17:49
 */
public interface PhotoInteract<T> {
    Subscription loadPhotos(RequestCallback<T> listener, int size, int page);
}
