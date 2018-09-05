package com.java.wuzihan.anews.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.java.wuzihan.anews.database.dao.CategoryDao;
import com.java.wuzihan.anews.database.dao.NewsDao;
import com.java.wuzihan.anews.database.entity.Category;
import com.java.wuzihan.anews.database.entity.News;

@Database(entities = {News.class, Category.class}, version = 1)
public abstract class ANewsDatabase extends RoomDatabase {

    public abstract NewsDao newsDao();

    public abstract CategoryDao categoryDao();

    private static ANewsDatabase INSTANCE;

    public static ANewsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ANewsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ANewsDatabase.class, "a_news_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .addCallback(sANewsDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     */
    private static RoomDatabase.Callback sANewsDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    /**
     * Populate the database in the background.
     * If you want to start with more words, just add them.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final CategoryDao mDao;

        PopulateDbAsync(ANewsDatabase db) {
            mDao = db.categoryDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            mDao.insertCategory(new Category("国内", true, "http://news.qq.com/newsgn/rss_newsgn.xml"));
            mDao.insertCategory(new Category("娱乐", true, "http://ent.qq.com/movie/jvrss_movie.xml"));
            mDao.insertCategory(new Category("财经", true, "http://finance.qq.com/financenews/breaknews/rss_finance.xml"));
            mDao.insertCategory(new Category("科技", true, "http://tech.qq.com/web/rss_web.xml"));
            mDao.insertCategory(new Category("体育", true, "http://sports.qq.com/rss_newssports.xml"));
            mDao.insertCategory(new Category("国际", true, "http://news.qq.com/newsgj/rss_newswj.xml"));
            mDao.insertCategory(new Category("游戏", true, "http://games.qq.com/ntgame/rss_ntgame.xml"));
            mDao.insertCategory(new Category("教育", true, "http://edu.qq.com/gaokao/rss_gaokao.xml"));
            mDao.insertCategory(new Category("动漫", true, "http://comic.qq.com/news/rss_news.xml"));
            mDao.insertCategory(new Category("时尚", true, "http://luxury.qq.com/staff/rss_staff.xml"));
            mDao.insertCategory(new Category("人物", true, "http://news.qq.com/person/rss_person.xml"));
            Log.d("ANewsDatabase", "inserting");
            return null;
        }
    }
}
