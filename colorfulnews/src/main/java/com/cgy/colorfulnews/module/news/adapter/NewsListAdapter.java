package com.cgy.colorfulnews.module.news.adapter;

import com.cgy.colorfulnews.entity.NewsSummary;
import com.cgy.colorfulnews.module.base.BaseRecyclerViewAdapter;

import javax.inject.Inject;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/12 14:19
 */
public class NewsListAdapter extends BaseRecyclerViewAdapter<NewsSummary> {

    @Inject
    public NewsListAdapter() {
        super(null);
    }

}
