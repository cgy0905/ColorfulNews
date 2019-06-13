/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com>
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
import com.kaku.colorfulnews.common.Constants;
import com.kaku.colorfulnews.greendao.NewsChannel;
import com.kaku.colorfulnews.listener.RequestCallback;
import com.kaku.colorfulnews.mvp.interactor.NewsChannelInteractor;
import com.kaku.colorfulnews.repository.db.NewsChannelManager;
import com.kaku.colorfulnews.utils.TransformUtils;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * @author 咖枯
 * @version 1.0 2016/6/30
 */
public class NewsChannelInteractorImpl implements NewsChannelInteractor<Map<Integer, List<NewsChannel>>> {

    private ExecutorService mSingleThreadPool;

    @Inject
    public NewsChannelInteractorImpl() {
    }

    @Override
    public Subscription lodeNewsChannels(final RequestCallback<Map<Integer, List<NewsChannel>>> callback) {
        return rx.Observable.create(new rx.Observable.OnSubscribe<Map<Integer, List<NewsChannel>>>() {
            @Override
            public void call(Subscriber<? super Map<Integer, List<NewsChannel>>> subscriber) {
                Map<Integer, List<NewsChannel>> newsChannelListMap = getNewsChannelData();
                subscriber.onNext(newsChannelListMap);
                subscriber.onCompleted();
            }

        }).compose(TransformUtils.<Map<Integer, List<NewsChannel>>>defaultSchedulers())
                .subscribe(new Subscriber<Map<Integer, List<NewsChannel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(App.getAppContext().getString(R.string.db_error));
                    }

                    @Override
                    public void onNext(Map<Integer, List<NewsChannel>> newsChannelListMap) {
                        callback.success(newsChannelListMap);
                    }
                });
    }

    private Map<Integer, List<NewsChannel>> getNewsChannelData() {
        Map<Integer, List<NewsChannel>> map = new HashMap<>();
        List<NewsChannel> channelListMine = NewsChannelManager.loadNewsChannelsMine();
        List<NewsChannel> channelListMore = NewsChannelManager.loadNewsChannelsMore();
        map.put(Constants.NEWS_CHANNEL_MINE, channelListMine);
        map.put(Constants.NEWS_CHANNEL_MORE, channelListMore);
        return map;
    }

    @Override
    public void swapDb(final int fromPosition, final int toPosition) {
        createThreadPool();
        mSingleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                KLog.d(Thread.currentThread().getName());
                KLog.d("fromPosition: " + fromPosition + "； toPosition: " + toPosition);

                NewsChannel fromNewsChannel = NewsChannelManager.loadNewsChannel(fromPosition);
                NewsChannel toNewsChannel = NewsChannelManager.loadNewsChannel(toPosition);

                if (isAdjacent(fromPosition, toPosition)) {
                    swapAdjacentIndexAndUpdate(fromNewsChannel, toNewsChannel);
                } else if (fromPosition - toPosition > 0) {
                    List<NewsChannel> newsChannels = NewsChannelManager
                            .loadNewsChannelsWithin(toPosition, fromPosition - 1);

                    increaseOrReduceIndexAndUpdate(newsChannels, true);
                    changeFromChannelIndexAndUpdate(fromNewsChannel, toPosition);
                } else if (fromPosition - toPosition < 0) {
                    List<NewsChannel> newsChannels = NewsChannelManager
                            .loadNewsChannelsWithin(fromPosition + 1, toPosition);

                    increaseOrReduceIndexAndUpdate(newsChannels, false);
                    changeFromChannelIndexAndUpdate(fromNewsChannel, toPosition);
                }
            }

            private boolean isAdjacent(int fromChannelIndex, int toChannelIndex) {
                return Math.abs(fromChannelIndex - toChannelIndex) == 1;
            }

            private void swapAdjacentIndexAndUpdate(NewsChannel fromNewsChannel,
                                                    NewsChannel toNewsChannel) {
                fromNewsChannel.setNewsChannelIndex(toPosition);
                toNewsChannel.setNewsChannelIndex(fromPosition);

                NewsChannelManager.update(fromNewsChannel);
                NewsChannelManager.update(toNewsChannel);
            }
        });
    }

    private void increaseOrReduceIndexAndUpdate(List<NewsChannel> newsChannels, boolean isIncrease) {
        for (NewsChannel newsChannel : newsChannels) {
            increaseOrReduceIndex(isIncrease, newsChannel);
            NewsChannelManager.update(newsChannel);
        }
    }

    private void increaseOrReduceIndex(boolean isIncrease, NewsChannel newsChannel) {
        int targetIndex;
        if (isIncrease) {
            targetIndex = newsChannel.getNewsChannelIndex() + 1;
        } else {
            targetIndex = newsChannel.getNewsChannelIndex() - 1;
        }
        newsChannel.setNewsChannelIndex(targetIndex);
    }

    private void changeFromChannelIndexAndUpdate(NewsChannel fromNewsChannel, int toPosition) {
        fromNewsChannel.setNewsChannelIndex(toPosition);
        NewsChannelManager.update(fromNewsChannel);
    }

    @Override
    public void updateDb(final NewsChannel newsChannel, final boolean isChannelMine) {
        createThreadPool();
        mSingleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                KLog.d(Thread.currentThread().getName());

                int channelIndex = newsChannel.getNewsChannelIndex();
                if (isChannelMine) {
                    List<NewsChannel> newsChannels = NewsChannelManager.loadNewsChannelsIndexGt(channelIndex);
                    increaseOrReduceIndexAndUpdate(newsChannels, false);

                    int targetIndex = NewsChannelManager.getAllSize();
                    ChangeIsSelectAndIndex(targetIndex, false);
                } else {
                    List<NewsChannel> newsChannels = NewsChannelManager.loadNewsChannelsIndexLtAndIsUnselected(channelIndex);
                    increaseOrReduceIndexAndUpdate(newsChannels, true);

                    int targetIndex = NewsChannelManager.getNewsChannelSelectSize();
                    ChangeIsSelectAndIndex(targetIndex, true);
                }

            }

            private void ChangeIsSelectAndIndex(int targetIndex, boolean isSelect) {
                newsChannel.setNewsChannelSelect(isSelect);
                changeFromChannelIndexAndUpdate(newsChannel, targetIndex);
            }
        });
    }

    private void createThreadPool() {
        if (mSingleThreadPool == null) {
            mSingleThreadPool = Executors.newSingleThreadExecutor();
        }
    }
}
