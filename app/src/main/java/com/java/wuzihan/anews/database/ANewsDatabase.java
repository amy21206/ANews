package com.java.wuzihan.anews.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.java.wuzihan.anews.database.dao.NewsDao;
import com.java.wuzihan.anews.database.entity.News;

@Database(entities = {News.class}, version = 1)
abstract class ANewsDatabase extends RoomDatabase {

    public abstract NewsDao newsDao();

    private static ANewsDatabase INSTANCE;

    static ANewsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ANewsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ANewsDatabase.class, "news_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
