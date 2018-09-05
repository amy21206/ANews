package com.java.wuzihan.anews.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "category_table")
public class Category {
    @PrimaryKey
    private String name;

    private boolean shown;
    private String url;

    public Category(String name, boolean showStatus, String url) {
        this.name = name;
        this.shown = showStatus;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public boolean isShown() {
        return shown;
    }

    public String getUrl() {
        return url;
    }
}
