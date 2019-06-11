package com.cgy.colorfulnews.module.base;


import android.support.annotation.NonNull;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/11 13:36
 */
public interface BasePresenter {

    void onCreate();

    void attachView(@NonNull BaseView view);

    void onDestroy();
}
