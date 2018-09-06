package com.java.wuzihan.anews.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.java.wuzihan.anews.database.entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("DELETE from category_table")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCategory(Category category);

    @Query("UPDATE category_table SET shown=:shown WHERE name=:name")
    void updateCategory(String name, boolean shown);

    @Query("SELECT * from category_table")
    LiveData<List<Category>> getAllCategories();

    @Query("SELECT * from category_table WHERE shown = 1")
    LiveData<List<Category>> getShownCategories();
}