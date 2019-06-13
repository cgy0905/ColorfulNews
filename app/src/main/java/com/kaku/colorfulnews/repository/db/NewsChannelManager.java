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
package com.kaku.colorfulnews.repository.db;

import com.kaku.colorfulnews.App;
import com.kaku.colorfulnews.R;
import com.kaku.colorfulnews.common.ApiConstants;
import com.kaku.colorfulnews.common.Constants;
import com.kaku.colorfulnews.greendao.NewsChannel;
import com.kaku.colorfulnews.greendao.NewsChannelDao;
import com.kaku.colorfulnews.utils.MyUtils;

import java.util.Arrays;
import java.util.List;

import de.greenrobot.dao.query.Query;

/**
 * @author 咖枯
 * @version 1.0 2016/6/2
 */
public class NewsChannelManager {

    /**
     * 首次打开程序初始化db
     */
    public static void initDB() {
        if (!MyUtils.getSharedPreferences().getBoolean(Constants.INIT_DB, false)) {
            NewsChannelDao dao = App.getNewsChannelDao();
            List<String> channelName = Arrays.asList(App.getAppContext().getResources()
                    .getStringArray(R.array.news_channel_name));
            List<String> channelId = Arrays.asList(App.getAppContext().getResources()
                    .getStringArray(R.array.news_channel_id));
            for (int i = 0; i < channelName.size(); i++) {
                NewsChannel entity = new NewsChannel(channelName.get(i), channelId.get(i)
                        , ApiConstants.getType(channelId.get(i)), i <= 5, i, i == 0);
                dao.insert(entity);
            }
            MyUtils.getSharedPreferences().edit().putBoolean(Constants.INIT_DB, true).apply();
        }
    }

    public static List<NewsChannel> loadNewsChannelsMine() {
        Query<NewsChannel> NewsChannelQuery = App.getNewsChannelDao().queryBuilder()
                .where(NewsChannelDao.Properties.NewsChannelSelect.eq(true))
                .orderAsc(NewsChannelDao.Properties.NewsChannelIndex).build();
        return NewsChannelQuery.list();
    }

    public static List<NewsChannel> loadNewsChannelsMore() {
        Query<NewsChannel> NewsChannelQuery = App.getNewsChannelDao().queryBuilder()
                .where(NewsChannelDao.Properties.NewsChannelSelect.eq(false))
                .orderAsc(NewsChannelDao.Properties.NewsChannelIndex).build();
        return NewsChannelQuery.list();
    }

    public static NewsChannel loadNewsChannel(int position) {
        return App.getNewsChannelDao().queryBuilder()
                .where(NewsChannelDao.Properties.NewsChannelIndex.eq(position)).build().unique();
    }

    public static void update(NewsChannel NewsChannel) {
        App.getNewsChannelDao().update(NewsChannel);
    }

    public static List<NewsChannel> loadNewsChannelsWithin(int from, int to) {
        Query<NewsChannel> NewsChannelQuery = App.getNewsChannelDao().queryBuilder()
                .where(NewsChannelDao.Properties.NewsChannelIndex
                        .between(from, to)).build();
        return NewsChannelQuery.list();
    }

    public static List<NewsChannel> loadNewsChannelsIndexGt(int channelIndex) {
        Query<NewsChannel> NewsChannelQuery = App.getNewsChannelDao().queryBuilder()
                .where(NewsChannelDao.Properties.NewsChannelIndex
                        .gt(channelIndex)).build();
        return NewsChannelQuery.list();
    }

    public static List<NewsChannel> loadNewsChannelsIndexLtAndIsUnselected(int channelIndex) {
        Query<NewsChannel> NewsChannelQuery = App.getNewsChannelDao().queryBuilder()
                .where(NewsChannelDao.Properties.NewsChannelIndex.lt(channelIndex),
                        NewsChannelDao.Properties.NewsChannelSelect.eq(false)).build();
        return NewsChannelQuery.list();
    }

    public static int getAllSize() {
        return App.getNewsChannelDao().loadAll().size();
    }

    public static int getNewsChannelSelectSize() {
        return (int) App.getNewsChannelDao().queryBuilder()
                .where(NewsChannelDao.Properties.NewsChannelSelect.eq(true))
                .buildCount().count();
    }
}
