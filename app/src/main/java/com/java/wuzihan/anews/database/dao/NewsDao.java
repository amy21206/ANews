package com.java.wuzihan.anews.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.java.wuzihan.anews.database.entity.News;

import java.util.List;

@Dao
public interface NewsDao {

    @Insert
    void insert(News news);

    @Query("DELETE FROM news_table")
    void deleteAll();

    @Query("SELECT * from news_table ORDER BY pubDate DESC")
    List<News> getAllNews();

    @Query("SELECT * from news_table WHERE category = :category ORDER BY pubDate DESC")
    List<News> getNewsOfCategory(String category);
}
