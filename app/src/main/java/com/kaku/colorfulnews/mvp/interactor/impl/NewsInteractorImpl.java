/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.kaku.colorfulnews.mvp.interactor.impl;

import com.kaku.colorfulnews.App;
import com.kaku.colorfulnews.R;
import com.kaku.colorfulnews.greendao.NewsChannel;
import com.kaku.colorfulnews.listener.RequestCallback;
import com.kaku.colorfulnews.mvp.interactor.NewsInteractor;
import com.kaku.colorfulnews.repository.db.NewsChannelManager;
import com.kaku.colorfulnews.utils.TransformUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * @author 咖枯
 * @version 1.0 2016/6/2
 */
public class NewsInteractorImpl implements NewsInteractor<List<NewsChannel>> {

    @Inject
    public NewsInteractorImpl() {
    }

    @Override
    public Subscription lodeNewsChannels(final RequestCallback<List<NewsChannel>> callback) {
        return Observable.create(new Observable.OnSubscribe<List<NewsChannel>>() {
            @Override
            public void call(Subscriber<? super List<NewsChannel>> subscriber) {
                NewsChannelManager.initDB();
                subscriber.onNext(NewsChannelManager.loadNewsChannelsMine());
                subscriber.onCompleted();
            }
        })
                .compose(TransformUtils.<List<NewsChannel>>defaultSchedulers())
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
