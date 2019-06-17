package com.cgy.colorfulnews.module.photo.interactor.impl;

import com.cgy.colorfulnews.App;
import com.cgy.colorfulnews.R;
import com.cgy.colorfulnews.common.HostType;
import com.cgy.colorfulnews.entity.PhotoGirl;
import com.cgy.colorfulnews.listener.RequestCallback;
import com.cgy.colorfulnews.module.photo.interactor.PhotoInteract;
import com.cgy.colorfulnews.repository.net.RetrofitManager;
import com.cgy.colorfulnews.utils.TransformUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/14 17:50
 */
public class PhotoInteractImpl implements PhotoInteract<List<PhotoGirl>> {

    @Inject
    public PhotoInteractImpl(){}

    @Override
    public Subscription loadPhotos(RequestCallback<List<PhotoGirl>> listener, int size, int page) {
        return RetrofitManager.getInstance(HostType.GANK_GIRL_PHOTO)
                .getPhotoListObservable(size, page)
                .map(girlData -> girlData.getResults())
                .compose(TransformUtils.defaultSchedulers())
                .subscribe(new Subscriber<List<PhotoGirl>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(App.getAppContext().getString(R.string.load_error));
                    }

                    @Override
                    public void onNext(List<PhotoGirl> photoGirls) {
                        listener.success(photoGirls);
                    }
                });
    }
}
