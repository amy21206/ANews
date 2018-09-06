package com.java.wuzihan.anews.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "news_table")
public class News {

    @NonNull
    @PrimaryKey
    private String heading;
    private String content;
    private String url;
    private String pubDate;
    private String category;
    private boolean viewed;
    private boolean favorite;

    public News(String category, String heading, String content, String url, String pubDate, boolean viewed, boolean favorite) {
        this.category = category;
        this.heading = heading;
        this.content = content;
        this.url = url;
        this.pubDate = pubDate;
        this.viewed = viewed;
        this.favorite = favorite;
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

    public boolean isFavorite() {
        return favorite;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}
