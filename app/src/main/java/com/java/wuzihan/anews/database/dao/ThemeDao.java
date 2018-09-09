package com.java.wuzihan.anews.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.java.wuzihan.anews.database.entity.Theme;

import java.util.List;

@Dao
public interface ThemeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Theme theme);

    @Query("SELECT * from theme_table where name=:name")
    Theme getThemeByName(String name);

    @Query("SELECT * from theme_table order by name asc")
    LiveData<List<Theme>> getThemes();

    @Query("SELECT * from theme_table where chose=1")
    Theme selectedTheme();

    @Query("UPDATE theme_table SET chose=:chose WHERE name=:name")
    void updateThemeChose(String name, boolean chose);
}
