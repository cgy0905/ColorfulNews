package com.cgy.colorfulnews.listener;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/11 13:46
 */
public interface RequestCallback<T> {

    void beforeRequest();

    void success(T data);

    void onError(String errorMsg);
}
