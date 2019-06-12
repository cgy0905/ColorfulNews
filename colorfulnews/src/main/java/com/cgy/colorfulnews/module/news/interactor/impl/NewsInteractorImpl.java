package com.cgy.colorfulnews.module.news.interactor.impl;

import com.cgy.colorfulnews.App;
import com.cgy.colorfulnews.R;
import com.cgy.colorfulnews.db.NewsChannel;
import com.cgy.colorfulnews.db.NewsChannelManager;
import com.cgy.colorfulnews.listener.RequestCallback;
import com.cgy.colorfulnews.module.news.interactor.NewsInteract;
import com.cgy.colorfulnews.utils.TransformUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/12 14:25
 */
public class NewsInteractorImpl implements NewsInteract<List<NewsChannel>> {

    @Inject
    public NewsInteractorImpl() {}

    @Override
    public Subscription loadNewsChannels(RequestCallback<List<NewsChannel>> callback) {
        return Observable.create((Observable.OnSubscribe<List<NewsChannel>>) subscriber -> {
            NewsChannelManager.initDB();
            subscriber.onNext(NewsChannelManager.loadNewsChannelsMine());
            subscriber.onCompleted();
        }).compose(TransformUtils.defaultSchedulers())
                .subscribe(new Subscriber<List<NewsChannel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(App.getAppContext().getString(R.string.db_error));
                    }

                    @Override
                    public void onNext(List<NewsChannel> newsChannels) {
                        callback.success(newsChannels);
                    }
                });
    }
}
