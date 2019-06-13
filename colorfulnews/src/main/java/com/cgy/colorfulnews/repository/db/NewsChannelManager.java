package com.cgy.colorfulnews.repository.db;

import com.cgy.colorfulnews.App;
import com.cgy.colorfulnews.R;
import com.cgy.colorfulnews.common.ApiConstants;
import com.cgy.colorfulnews.common.Constants;
import com.cgy.colorfulnews.db.NewsChannel;
import com.cgy.colorfulnews.db.NewsChannelDao;
import com.cgy.colorfulnews.utils.MyUtils;

import org.greenrobot.greendao.query.Query;

import java.util.Arrays;
import java.util.List;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/12 14:29
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
                NewsChannel entity = new NewsChannel(channelName.get(i), channelId.get(i),
                        ApiConstants.getType(channelId.get(i)), i <= 5, i, i == 0);
                dao.insert(entity);
            }
            MyUtils.getSharedPreferences().edit().putBoolean(Constants.INIT_DB, true).apply();
        }
    }

    public static List<NewsChannel> loadNewsChannelsMine() {
        Query<NewsChannel> newsChannelQuery = App.getNewsChannelDao().queryBuilder()
                .where(NewsChannelDao.Properties.NewsChannelSelect.eq(true))
                .orderAsc(NewsChannelDao.Properties.NewsChannelIndex).build();
        return newsChannelQuery.list();
    }

    public static List<NewsChannel> loadNewsChannelsMore() {
        Query<NewsChannel> newsChannelQuery = App.getNewsChannelDao().queryBuilder()
                .where(NewsChannelDao.Properties.NewsChannelSelect.eq(true))
                .orderAsc(NewsChannelDao.Properties.NewsChannelIndex).build();
        return newsChannelQuery.list();
    }

    public static NewsChannel loadNewsChannel(int position) {
        return App.getNewsChannelDao().queryBuilder()
                .where(NewsChannelDao.Properties.NewsChannelIndex.eq(position)).build().unique();
    }

    public static void update(NewsChannel newsChannel) {
        App.getNewsChannelDao().update(newsChannel);
    }

    public static List<NewsChannel> loadNewsChannelsWithin(int from, int to) {
        Query<NewsChannel> newsChannelQuery = App.getNewsChannelDao().queryBuilder()
                .where(NewsChannelDao.Properties.NewsChannelIndex.between(from, to)).build();
        return newsChannelQuery.list();
    }

    public static List<NewsChannel> loadNewsChannelsIndexGt(int channelIndex) {
        Query<NewsChannel> newsChannelQuery = App.getNewsChannelDao().queryBuilder()
                .where(NewsChannelDao.Properties.NewsChannelIndex.gt(channelIndex)).build();
        return newsChannelQuery.list();
    }

    public static List<NewsChannel> loadNewsChannelsIndexLtAndIsUnselected(int channelIndex) {
        Query<NewsChannel> newsChannelQuery = App.getNewsChannelDao().queryBuilder()
                .where(NewsChannelDao.Properties.NewsChannelIndex.lt(channelIndex),
                        NewsChannelDao.Properties.NewsChannelSelect.eq(false)).build();
        return newsChannelQuery.list();
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
