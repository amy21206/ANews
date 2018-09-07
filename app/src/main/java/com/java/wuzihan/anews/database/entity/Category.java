package com.java.wuzihan.anews.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "category_table")
public class Category {

    @NonNull
    @PrimaryKey
    private String name;

    private boolean shown;
    private String url;

    public Category(@NonNull String name, boolean shown, String url) {
        this.name = name;
        this.shown = shown;
        this.url = url;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public boolean isShown() {
        return shown;
    }

    public String getUrl() {
        return url;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }
}
