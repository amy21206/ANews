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
    private int readtime;

    public Category(@NonNull String name, boolean shown, String url, int readtime) {
        this.name = name;
        this.shown = shown;
        this.url = url;
        this.readtime = readtime;
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

    public int getReadtime() {
        return readtime;
    }

    public void setReadtime(int readtime) {
        this.readtime = readtime;
    }
}
