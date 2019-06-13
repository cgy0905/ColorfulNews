package com.cgy.colorfulnews.module.news.interactor.impl;

import com.cgy.colorfulnews.App;
import com.cgy.colorfulnews.R;
import com.cgy.colorfulnews.common.Constants;
import com.cgy.colorfulnews.db.NewsChannel;
import com.cgy.colorfulnews.listener.RequestCallback;
import com.cgy.colorfulnews.module.news.interactor.NewsChannelInteract;
import com.cgy.colorfulnews.repository.db.NewsChannelManager;
import com.cgy.colorfulnews.utils.TransformUtils;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/13 14:54
 */
public class NewsChannelInteractImpl implements NewsChannelInteract<Map<Integer, List<NewsChannel>>> {

    private ExecutorService mSingleThreadPool;

    @Inject
    public NewsChannelInteractImpl() {
    }

    @Override
    public Subscription loadNewsChannels(RequestCallback<Map<Integer, List<NewsChannel>>> callback) {
        return Observable.create((Observable.OnSubscribe<Map<Integer, List<NewsChannel>>>) subscriber -> {
            Map<Integer, List<NewsChannel>> newsChannelListMap = getNewsChannelData();
            subscriber.onNext(newsChannelListMap);
            subscriber.onCompleted();
        }).compose(TransformUtils.defaultSchedulers())
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
    public void swapDb(int fromPosition, int toPosition) {
        createThreadPool();
        mSingleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                KLog.d(Thread.currentThread().getName());
                KLog.d("fromPosition: " + fromPosition + "; toPosition: " + toPosition);

                NewsChannel fromNewsChannel = NewsChannelManager.loadNewsChannel(fromPosition);
                NewsChannel toNewsChannel = NewsChannelManager.loadNewsChannel(toPosition);

                if (isAdjacent(fromPosition, toPosition)) {
                    swapAdjacentIndexAndUpdate(fromNewsChannel, toNewsChannel);
                } else if (fromPosition - toPosition > 0) {
                    List<NewsChannel> newsChannels = NewsChannelManager.loadNewsChannelsWithin(toPosition, fromPosition - 1);

                    increaseOrReduceIndexAndUpdate(newsChannels, true);
                    changeFromChannelIndexAndUpdate(fromNewsChannel, toPosition);
                } else if (fromPosition - toPosition < 0) {
                    List<NewsChannel> newsChannels = NewsChannelManager.loadNewsChannelsWithin(fromPosition + 1, toPosition);

                    increaseOrReduceIndexAndUpdate(newsChannels, false);
                    changeFromChannelIndexAndUpdate(fromNewsChannel, toPosition);
                }
            }

            private boolean isAdjacent(int fromChannelIndex, int toChannelIndex) {
                return Math.abs(fromChannelIndex - toChannelIndex) == 1;
            }

            private void swapAdjacentIndexAndUpdate(NewsChannel fromNewsChannel, NewsChannel toNewsChannel) {
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
    public void updateDb(NewsChannel newsChannel, boolean isChannelMine) {
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
                    changeIsSelectAndIndex(targetIndex, false);
                } else {
                    List<NewsChannel> newsChannels = NewsChannelManager.loadNewsChannelsIndexLtAndIsUnselected(channelIndex);
                    increaseOrReduceIndexAndUpdate(newsChannels, true);

                    int targetIndex = NewsChannelManager.getNewsChannelSelectSize();
                    changeIsSelectAndIndex(targetIndex, true);
                }
            }

            private void changeIsSelectAndIndex(int targetIndex, boolean isSelect) {
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
