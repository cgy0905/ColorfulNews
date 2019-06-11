package com.kaku.colorfulnews.greendao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.kaku.colorfulnews.greendao.NewsChannelTable;

import com.kaku.colorfulnews.greendao.NewsChannelTableDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig newsChannelTableDaoConfig;

    private final NewsChannelTableDao newsChannelTableDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        newsChannelTableDaoConfig = daoConfigMap.get(NewsChannelTableDao.class).clone();
        newsChannelTableDaoConfig.initIdentityScope(type);

        newsChannelTableDao = new NewsChannelTableDao(newsChannelTableDaoConfig, this);

        registerDao(NewsChannelTable.class, newsChannelTableDao);
    }
    
    public void clear() {
        newsChannelTableDaoConfig.getIdentityScope().clear();
    }

    public NewsChannelTableDao getNewsChannelTableDao() {
        return newsChannelTableDao;
    }

}
