package com.cgy.colorfulnews.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/10 16:38
 */
@Entity
public class NewsChannel {

    @Id(autoincrement = true)
    private Long id;
    /**
     * 频道名称
     */
    @NotNull
    @Index(name = "newsChannelName", unique = true)
    private String newsChannelName;

    /**
     * 频道id
     */
    @NotNull
    private String newsChannelId;


    /**
     * 频道类型
     */
    @NotNull
    private String newsChannelType;


    /**
     * 选中的频道
     */
    @NotNull
    private boolean newsChannelSelect;

    /**
     * 频道的排序位置
     */
    @NotNull
    private int newsChannelIndex;

    /**
     * 频道是否时固定的
     */
    private boolean newsChannelFixed;

    @Generated(hash = 576877971)
    public NewsChannel(Long id, @NotNull String newsChannelName,
            @NotNull String newsChannelId, @NotNull String newsChannelType,
            boolean newsChannelSelect, int newsChannelIndex,
            boolean newsChannelFixed) {
        this.id = id;
        this.newsChannelName = newsChannelName;
        this.newsChannelId = newsChannelId;
        this.newsChannelType = newsChannelType;
        this.newsChannelSelect = newsChannelSelect;
        this.newsChannelIndex = newsChannelIndex;
        this.newsChannelFixed = newsChannelFixed;
    }

    @Generated(hash = 566079451)
    public NewsChannel() {
    }

    public String getNewsChannelName() {
        return this.newsChannelName;
    }

    public void setNewsChannelName(String newsChannelName) {
        this.newsChannelName = newsChannelName;
    }

    public String getNewsChannelId() {
        return this.newsChannelId;
    }

    public void setNewsChannelId(String newsChannelId) {
        this.newsChannelId = newsChannelId;
    }

    public String getNewsChannelType() {
        return this.newsChannelType;
    }

    public void setNewsChannelType(String newsChannelType) {
        this.newsChannelType = newsChannelType;
    }

    public boolean getNewsChannelSelect() {
        return this.newsChannelSelect;
    }

    public void setNewsChannelSelect(boolean newsChannelSelect) {
        this.newsChannelSelect = newsChannelSelect;
    }

    public int getNewsChannelIndex() {
        return this.newsChannelIndex;
    }

    public void setNewsChannelIndex(int newsChannelIndex) {
        this.newsChannelIndex = newsChannelIndex;
    }

    public boolean getNewsChannelFixed() {
        return this.newsChannelFixed;
    }

    public void setNewsChannelFixed(boolean newsChannelFixed) {
        this.newsChannelFixed = newsChannelFixed;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
}
