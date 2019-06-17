package com.cgy.colorfulnews.module.photo.presenter.impl;

import com.cgy.colorfulnews.common.LoadNewsType;
import com.cgy.colorfulnews.entity.PhotoGirl;
import com.cgy.colorfulnews.listener.RequestCallback;
import com.cgy.colorfulnews.module.base.BasePresenterImpl;
import com.cgy.colorfulnews.module.photo.interactor.impl.PhotoInteractImpl;
import com.cgy.colorfulnews.module.photo.presenter.PhotoPresenter;
import com.cgy.colorfulnews.module.photo.view.PhotoView;

import java.util.List;

import javax.inject.Inject;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/14 17:39
 */
public class PhotoPresenterImpl extends BasePresenterImpl<PhotoView, List<PhotoGirl>>
        implements PhotoPresenter, RequestCallback<List<PhotoGirl>> {

    private PhotoInteractImpl mPhotoInteract;
    private static int SIZE = 20;
    private int mStartPage = 1;
    private boolean mIsFirstLoad;
    private boolean mIsRefresh = true;

    @Inject
    public PhotoPresenterImpl(PhotoInteractImpl photoInteract) {
        mPhotoInteract = photoInteract;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadPhotoData();
    }

    @Override
    public void beforeRequest() {
        if (!mIsFirstLoad) {
            mView.showProgress();
        }
    }

    @Override
    public void onError(String errorMsg) {
        super.onError(errorMsg);
        if (mView != null) {
            int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_ERROR : LoadNewsType.TYPE_LOAD_MORE_ERROR;
            mView.setPhotoList(null, loadType);
        }
    }

    @Override
    public void success(List<PhotoGirl> data) {
        super.success(data);
        mIsFirstLoad = true;
        if (data != null) {
            mStartPage += 1;

        }
        int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_SUCCESS : LoadNewsType.TYPE_LOAD_MORE_SUCCESS;
        if (mView != null) {
            mView.setPhotoList(data, loadType);
            mView.hideProgress();
        }
    }

    @Override
    public void refreshData() {
        mStartPage = 1;
        mIsRefresh = true;
        loadPhotoData();
    }

    @Override
    public void loadMore() {
        mIsRefresh = false;
        loadPhotoData();
    }

    private void loadPhotoData() {
        mPhotoInteract.loadPhotos(this, SIZE, mStartPage);
    }
}
