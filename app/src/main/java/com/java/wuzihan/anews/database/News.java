package com.java.wuzihan.anews.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "news_table")
public class News {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String mHeading;
    private String mContent;
    private String mUrl;
    private String mPubDate;

    public News(String heading, String content, String url, String pubDate) {
        mHeading = heading;
        mContent = content;
        mUrl = url;
        mPubDate = pubDate;
    }

    public void setId(int id_) {
        id = id_;
    }

    public String getHeading() {
        return mHeading;
    }

    public String getContent() {
        return mContent;
    }

    public String getPubDate() {
        return mPubDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public int getId() {
        return id;
    }
}
