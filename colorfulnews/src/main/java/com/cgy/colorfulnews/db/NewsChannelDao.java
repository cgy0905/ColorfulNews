package com.cgy.colorfulnews.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NEWS_CHANNEL".
*/
public class NewsChannelDao extends AbstractDao<NewsChannel, Long> {

    public static final String TABLENAME = "NEWS_CHANNEL";

    /**
     * Properties of entity NewsChannel.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property NewsChannelName = new Property(1, String.class, "newsChannelName", false, "NEWS_CHANNEL_NAME");
        public final static Property NewsChannelId = new Property(2, String.class, "newsChannelId", false, "NEWS_CHANNEL_ID");
        public final static Property NewsChannelType = new Property(3, String.class, "newsChannelType", false, "NEWS_CHANNEL_TYPE");
        public final static Property NewsChannelSelect = new Property(4, boolean.class, "newsChannelSelect", false, "NEWS_CHANNEL_SELECT");
        public final static Property NewsChannelIndex = new Property(5, int.class, "newsChannelIndex", false, "NEWS_CHANNEL_INDEX");
        public final static Property NewsChannelFixed = new Property(6, boolean.class, "newsChannelFixed", false, "NEWS_CHANNEL_FIXED");
    }


    public NewsChannelDao(DaoConfig config) {
        super(config);
    }
    
    public NewsChannelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NEWS_CHANNEL\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NEWS_CHANNEL_NAME\" TEXT NOT NULL ," + // 1: newsChannelName
                "\"NEWS_CHANNEL_ID\" TEXT NOT NULL ," + // 2: newsChannelId
                "\"NEWS_CHANNEL_TYPE\" TEXT NOT NULL ," + // 3: newsChannelType
                "\"NEWS_CHANNEL_SELECT\" INTEGER NOT NULL ," + // 4: newsChannelSelect
                "\"NEWS_CHANNEL_INDEX\" INTEGER NOT NULL ," + // 5: newsChannelIndex
                "\"NEWS_CHANNEL_FIXED\" INTEGER NOT NULL );"); // 6: newsChannelFixed
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "newsChannelName ON \"NEWS_CHANNEL\"" +
                " (\"NEWS_CHANNEL_NAME\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NEWS_CHANNEL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, NewsChannel entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getNewsChannelName());
        stmt.bindString(3, entity.getNewsChannelId());
        stmt.bindString(4, entity.getNewsChannelType());
        stmt.bindLong(5, entity.getNewsChannelSelect() ? 1L: 0L);
        stmt.bindLong(6, entity.getNewsChannelIndex());
        stmt.bindLong(7, entity.getNewsChannelFixed() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, NewsChannel entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getNewsChannelName());
        stmt.bindString(3, entity.getNewsChannelId());
        stmt.bindString(4, entity.getNewsChannelType());
        stmt.bindLong(5, entity.getNewsChannelSelect() ? 1L: 0L);
        stmt.bindLong(6, entity.getNewsChannelIndex());
        stmt.bindLong(7, entity.getNewsChannelFixed() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public NewsChannel readEntity(Cursor cursor, int offset) {
        NewsChannel entity = new NewsChannel( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // newsChannelName
            cursor.getString(offset + 2), // newsChannelId
            cursor.getString(offset + 3), // newsChannelType
            cursor.getShort(offset + 4) != 0, // newsChannelSelect
            cursor.getInt(offset + 5), // newsChannelIndex
            cursor.getShort(offset + 6) != 0 // newsChannelFixed
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, NewsChannel entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNewsChannelName(cursor.getString(offset + 1));
        entity.setNewsChannelId(cursor.getString(offset + 2));
        entity.setNewsChannelType(cursor.getString(offset + 3));
        entity.setNewsChannelSelect(cursor.getShort(offset + 4) != 0);
        entity.setNewsChannelIndex(cursor.getInt(offset + 5));
        entity.setNewsChannelFixed(cursor.getShort(offset + 6) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(NewsChannel entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(NewsChannel entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(NewsChannel entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
