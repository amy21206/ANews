package com.java.wuzihan.anews.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "theme_table")
public class Theme {

    @NonNull
    @PrimaryKey
    String name;
    int noAppBarId;
    int appBarId;
    boolean chose;

    public Theme(String name, int appBarId, int noAppBarId, boolean chose) {
        this.name = name;
        this.noAppBarId = noAppBarId;
        this.appBarId = appBarId;
        this.chose = chose;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public int getAppBarId() {
        return appBarId;
    }

    public int getNoAppBarId() {
        return noAppBarId;
    }

    public boolean isChose() {
        return chose;
    }

    public void setChose(boolean chose) {
        this.chose = chose;
    }
}
