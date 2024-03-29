package com.kaku.colorfulnews.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.kaku.colorfulnews.greendao.NewsChannel;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table NEWS_CHANNEL.
*/
public class NewsChannelDao extends AbstractDao<NewsChannel, String> {

    public static final String TABLENAME = "NEWS_CHANNEL";

    /**
     * Properties of entity NewsChannel.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property NewsChannelName = new Property(0, String.class, "newsChannelName", true, "NEWS_CHANNEL_NAME");
        public final static Property NewsChannelId = new Property(1, String.class, "newsChannelId", false, "NEWS_CHANNEL_ID");
        public final static Property NewsChannelType = new Property(2, String.class, "newsChannelType", false, "NEWS_CHANNEL_TYPE");
        public final static Property NewsChannelSelect = new Property(3, boolean.class, "newsChannelSelect", false, "NEWS_CHANNEL_SELECT");
        public final static Property NewsChannelIndex = new Property(4, int.class, "newsChannelIndex", false, "NEWS_CHANNEL_INDEX");
        public final static Property NewsChannelFixed = new Property(5, Boolean.class, "newsChannelFixed", false, "NEWS_CHANNEL_FIXED");
    };


    public NewsChannelDao(DaoConfig config) {
        super(config);
    }
    
    public NewsChannelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'NEWS_CHANNEL' (" + //
                "'NEWS_CHANNEL_NAME' TEXT PRIMARY KEY NOT NULL ," + // 0: newsChannelName
                "'NEWS_CHANNEL_ID' TEXT NOT NULL ," + // 1: newsChannelId
                "'NEWS_CHANNEL_TYPE' TEXT NOT NULL ," + // 2: newsChannelType
                "'NEWS_CHANNEL_SELECT' INTEGER NOT NULL ," + // 3: newsChannelSelect
                "'NEWS_CHANNEL_INDEX' INTEGER NOT NULL ," + // 4: newsChannelIndex
                "'NEWS_CHANNEL_FIXED' INTEGER);"); // 5: newsChannelFixed
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_NEWS_CHANNEL_NEWS_CHANNEL_NAME ON NEWS_CHANNEL" +
                " (NEWS_CHANNEL_NAME);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'NEWS_CHANNEL'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, NewsChannel entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getNewsChannelName());
        stmt.bindString(2, entity.getNewsChannelId());
        stmt.bindString(3, entity.getNewsChannelType());
        stmt.bindLong(4, entity.getNewsChannelSelect() ? 1l: 0l);
        stmt.bindLong(5, entity.getNewsChannelIndex());
 
        Boolean newsChannelFixed = entity.getNewsChannelFixed();
        if (newsChannelFixed != null) {
            stmt.bindLong(6, newsChannelFixed ? 1l: 0l);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public NewsChannel readEntity(Cursor cursor, int offset) {
        NewsChannel entity = new NewsChannel( //
            cursor.getString(offset + 0), // newsChannelName
            cursor.getString(offset + 1), // newsChannelId
            cursor.getString(offset + 2), // newsChannelType
            cursor.getShort(offset + 3) != 0, // newsChannelSelect
            cursor.getInt(offset + 4), // newsChannelIndex
            cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0 // newsChannelFixed
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, NewsChannel entity, int offset) {
        entity.setNewsChannelName(cursor.getString(offset + 0));
        entity.setNewsChannelId(cursor.getString(offset + 1));
        entity.setNewsChannelType(cursor.getString(offset + 2));
        entity.setNewsChannelSelect(cursor.getShort(offset + 3) != 0);
        entity.setNewsChannelIndex(cursor.getInt(offset + 4));
        entity.setNewsChannelFixed(cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(NewsChannel entity, long rowId) {
        return entity.getNewsChannelName();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(NewsChannel entity) {
        if(entity != null) {
            return entity.getNewsChannelName();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
