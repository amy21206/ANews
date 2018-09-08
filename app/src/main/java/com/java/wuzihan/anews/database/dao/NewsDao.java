package com.java.wuzihan.anews.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.java.wuzihan.anews.database.entity.News;

import java.util.List;

@Dao
public interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(News news);

    @Query("DELETE FROM news_table")
    void deleteAll();

    @Query("SELECT * from news_table ORDER BY pubDate DESC")
    LiveData<List<News>> getAllNews();

    @Query("SELECT * from news_table WHERE category = :category ORDER BY pubDate DESC")
    LiveData<List<News>> getNewsOfCategory(String category);

    @Query("SELECT * from news_table WHERE category = :category ORDER BY pubDate DESC LIMIT :newsNum")
    LiveData<List<News>> getNewsOfCategoryByNumber(String category, int newsNum);

    @Query("SELECT * from news_table WHERE favorite = 1 ORDER BY pubDate DESC")
    LiveData<List<News>> getNewsFavorite();

    /*"SELECT * FROM user WHERE first_name LIKE :search "
            + "OR last_name LIKE :search"
    */
    @Query("SELECT * from news_table WHERE heading LIKE '%' || :prompt || '%'" + "OR content LIKE '%' || :prompt || '%'")
    LiveData<List<News>> getNewsSearchBy(String prompt);

    @Query("UPDATE news_table SET viewed=:viewed WHERE heading=:heading")
    void updateNewsViewed(String heading, boolean viewed);

    @Query("UPDATE news_table SET favorite=:favorite WHERE heading=:heading")
    void updateNewsFavorite(String heading, boolean favorite);

    @Query("SELECT * FROM news_table WHERE heading=:heading")
    News getNewsByHeading(String heading);

    @Query("SELECT * FROM news_table WHERE viewed=0 and category like :cat1 or :cat2 or :cat3 LIMIT :num")
    LiveData<List<News>> getNewsRecommend(String cat1, String cat2, String cat3, int num);
}
