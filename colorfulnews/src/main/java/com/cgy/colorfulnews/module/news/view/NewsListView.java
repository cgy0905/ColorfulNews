package com.cgy.colorfulnews.module.news.view;

import com.cgy.colorfulnews.common.LoadNewsType;
import com.cgy.colorfulnews.entity.NewsSummary;
import com.cgy.colorfulnews.module.base.BaseView;

import java.util.List;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/12 14:13
 */
public interface NewsListView extends BaseView {

    void setNewsList(List<NewsSummary> newsSummary, @LoadNewsType.checker int loadType);
}
