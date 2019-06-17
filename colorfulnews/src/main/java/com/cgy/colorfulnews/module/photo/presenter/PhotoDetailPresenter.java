package com.cgy.colorfulnews.module.photo.presenter;

import com.cgy.colorfulnews.common.PhotoRequestType;
import com.cgy.colorfulnews.module.base.BasePresenter;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/17 11:20
 */
public interface PhotoDetailPresenter extends BasePresenter {
    void handlePicture(String imageUrl, @PhotoRequestType.PhotoRequestTypeChecker int type);
}
