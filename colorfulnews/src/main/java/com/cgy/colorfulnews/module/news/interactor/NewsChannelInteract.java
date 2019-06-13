package com.cgy.colorfulnews.module.news.interactor;

import com.cgy.colorfulnews.db.NewsChannel;
import com.cgy.colorfulnews.listener.RequestCallback;

import rx.Subscription;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/13 14:52
 */
public interface NewsChannelInteract<T> {
    Subscription loadNewsChannels(RequestCallback<T> callback);

    void swapDb(int fromPosition, int toPosition);

    void updateDb(NewsChannel newsChannel, boolean isChannelMine);
}
