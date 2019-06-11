package com.cgy.colorfulnews.module.base;

import android.support.annotation.NonNull;

import com.cgy.colorfulnews.listener.RequestCallback;
import com.cgy.colorfulnews.utils.MyUtils;

import rx.Subscription;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/11 13:42
 */
public class BasePresenterImpl<T extends BaseView, E> implements BasePresenter, RequestCallback<E> {

    protected T mView;
    protected Subscription mSubscription;


    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        MyUtils.cancelSubscription(mSubscription);
    }

    @Override
    public void attachView(@NonNull BaseView view) {

        mView = (T) view;
    }

    @Override
    public void beforeRequest() {
        mView.showProgress();
    }

    @Override
    public void success(E data) {
        mView.hideProgress();
    }

    @Override
    public void onError(String errorMsg) {
        mView.hideProgress();
        mView.showMsg(errorMsg);
    }






}
