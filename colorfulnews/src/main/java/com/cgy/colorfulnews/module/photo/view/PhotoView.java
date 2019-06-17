package com.cgy.colorfulnews.module.photo.view;

import com.cgy.colorfulnews.common.LoadNewsType;
import com.cgy.colorfulnews.entity.PhotoGirl;
import com.cgy.colorfulnews.module.base.BaseView;

import java.util.List;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/14 17:24
 */
public interface PhotoView extends BaseView {
    void setPhotoList(List<PhotoGirl> photoGirls, @LoadNewsType.checker int loadType);
}
