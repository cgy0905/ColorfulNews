package com.cgy.colorfulnews.module.news.presenter;

import com.cgy.colorfulnews.db.NewsChannel;
import com.cgy.colorfulnews.module.base.BasePresenter;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/13 13:56
 */
public interface NewsChannelPresenter extends BasePresenter {
    void onItemSwap(int fromPosition, int toPosition);

    void onIteAddOrRemove(NewsChannel newsChannel, boolean isChannelMine);
}
