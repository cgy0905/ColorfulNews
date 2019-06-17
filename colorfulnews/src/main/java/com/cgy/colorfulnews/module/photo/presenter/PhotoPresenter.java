package com.cgy.colorfulnews.module.photo.presenter;

import com.cgy.colorfulnews.module.base.BasePresenter;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/14 17:38
 */
public interface PhotoPresenter extends BasePresenter {
    void refreshData();

    void loadMore();
}
