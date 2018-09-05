package com.java.wuzihan.anews.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "news_table")
public class News {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String heading;
    private String content;
    private String url;
    private String pubDate;
    private String category;

    public News(String category, String heading, String content, String url, String pubDate) {
        this.category = category;
        this.heading = heading;
        this.content = content;
        this.url = url;
        this.pubDate = pubDate;
    }

    public void setId(int id_) {
        id = id_;
    }

    public String getHeading() {
        return heading;
    }

    public String getContent() {
        return content;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getUrl() {
        return url;
    }

    public String getCategory() {
        return category;
    }

    public int getId() {
        return id;
    }
}
