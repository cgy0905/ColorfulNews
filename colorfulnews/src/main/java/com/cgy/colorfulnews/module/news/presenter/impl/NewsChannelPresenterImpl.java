package com.cgy.colorfulnews.module.news.presenter.impl;

import com.cgy.colorfulnews.common.Constants;
import com.cgy.colorfulnews.db.NewsChannel;
import com.cgy.colorfulnews.event.ChannelChangeEvent;
import com.cgy.colorfulnews.module.base.BasePresenterImpl;
import com.cgy.colorfulnews.module.news.interactor.impl.NewsChannelInteractImpl;
import com.cgy.colorfulnews.module.news.presenter.NewsChannelPresenter;
import com.cgy.colorfulnews.module.news.view.NewsChannelView;
import com.cgy.colorfulnews.utils.RxBus;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/13 13:58
 */
public class NewsChannelPresenterImpl extends BasePresenterImpl<NewsChannelView, Map<Integer, List<NewsChannel>>> implements NewsChannelPresenter {

    private NewsChannelInteractImpl mNewsChannelInteract;
    private boolean mIsChannelChanged;

    @Inject
    public NewsChannelPresenterImpl(NewsChannelInteractImpl newsChannelInteract){
        mNewsChannelInteract = newsChannelInteract;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mIsChannelChanged) {
            RxBus.getInstance().post(new ChannelChangeEvent());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNewsChannelInteract.loadNewsChannels(this);
    }

    @Override
    public void success(Map<Integer, List<NewsChannel>> data) {
        super.success(data);
        mView.initRecyclerView(data.get(Constants.NEWS_CHANNEL_MINE), data.get(Constants.NEWS_CHANNEL_MORE));
    }

    @Override
    public void onItemSwap(int fromPosition, int toPosition) {
        mNewsChannelInteract.swapDb(fromPosition, toPosition);
        mIsChannelChanged = true;
    }

    @Override
    public void onIteAddOrRemove(NewsChannel newsChannel, boolean isChannelMine) {
        mNewsChannelInteract.updateDb(newsChannel, isChannelMine);
        mIsChannelChanged = true;
    }
}
