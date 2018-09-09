package com.java.wuzihan.anews.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.java.wuzihan.anews.R;
import com.java.wuzihan.anews.database.dao.CategoryDao;
import com.java.wuzihan.anews.database.dao.NewsDao;
import com.java.wuzihan.anews.database.dao.ThemeDao;
import com.java.wuzihan.anews.database.entity.Category;
import com.java.wuzihan.anews.database.entity.News;
import com.java.wuzihan.anews.database.entity.Theme;

@Database(entities = {News.class, Category.class, Theme.class}, version = 1)
public abstract class ANewsDatabase extends RoomDatabase {

    public abstract NewsDao newsDao();

    public abstract CategoryDao categoryDao();

    public abstract ThemeDao themeDao();

    private static ANewsDatabase INSTANCE;

    public static ANewsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ANewsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ANewsDatabase.class, "a_news_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
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
            new PopulateDbOnStartAsync(INSTANCE).execute();
        }
    };

    /**
     * Populate the database in the background.
     * If you want to start with more words, just add them.
     */
    private static class PopulateDbOnStartAsync extends AsyncTask<Void, Void, Void> {

        private final CategoryDao categoryDao;
        private final ThemeDao themeDao;

        PopulateDbOnStartAsync(ANewsDatabase db) {
            categoryDao = db.categoryDao();
            themeDao = db.themeDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            // populating categories
            categoryDao.insertCategory(new Category("国内", true, "http://news.qq.com/newsgn/rss_newsgn.xml", 0));
            categoryDao.insertCategory(new Category("娱乐", true, "http://ent.qq.com/movie/rss_movie.xml", 0));
            categoryDao.insertCategory(new Category("财经", true, "http://finance.qq.com/financenews/breaknews/rss_finance.xml", 0));
            categoryDao.insertCategory(new Category("科技", true, "http://tech.qq.com/web/rss_web.xml", 0));
            categoryDao.insertCategory(new Category("体育", true, "http://sports.qq.com/rss_newssports.xml", 0));
            categoryDao.insertCategory(new Category("国际", true, "http://news.qq.com/newsgj/rss_newswj.xml", 0));
            categoryDao.insertCategory(new Category("游戏", true, "http://games.qq.com/ntgame/rss_ntgame.xml", 0));
            categoryDao.insertCategory(new Category("教育", true, "http://edu.qq.com/gaokao/rss_gaokao.xml", 0));
            categoryDao.insertCategory(new Category("动漫", false, "http://comic.qq.com/news/rss_news.xml", 0));
            categoryDao.insertCategory(new Category("时尚", false, "http://luxury.qq.com/staff/rss_staff.xml", 0));
            categoryDao.insertCategory(new Category("人物", false, "http://news.qq.com/person/rss_person.xml", 0));
            Log.d("ANewsDatabase", "inserting");

            themeDao.insert(new Theme("RED", R.style.AppTheme_RED, R.style.AppTheme_RED_NoActionBar, true));
            themeDao.insert(new Theme("PINK", R.style.AppTheme_PINK, R.style.AppTheme_PINK_NoActionBar, false));
            themeDao.insert(new Theme("PURPLE", R.style.AppTheme_PURPLE, R.style.AppTheme_PURPLE_NoActionBar, false));
            themeDao.insert(new Theme("DEEPPURPLE", R.style.AppTheme_DEEPPURPLE, R.style.AppTheme_DEEPPURPLE_NoActionBar, false));
            themeDao.insert(new Theme("INDIGO", R.style.AppTheme_INDIGO, R.style.AppTheme_INDIGO_NoActionBar, false));
            themeDao.insert(new Theme("BLUE", R.style.AppTheme_BLUE, R.style.AppTheme_BLUE_NoActionBar, false));
            themeDao.insert(new Theme("LIGHTBLUE", R.style.AppTheme_LIGHTBLUE, R.style.AppTheme_LIGHTBLUE_NoActionBar, false));
            themeDao.insert(new Theme("CYAN", R.style.AppTheme_CYAN, R.style.AppTheme_CYAN_NoActionBar, false));
            themeDao.insert(new Theme("TEAL", R.style.AppTheme_TEAL, R.style.AppTheme_TEAL_NoActionBar, false));
            themeDao.insert(new Theme("GREEN", R.style.AppTheme_GREEN, R.style.AppTheme_GREEN_NoActionBar, false));
            themeDao.insert(new Theme("LIGHTGREEN", R.style.AppTheme_LIGHTGREEN, R.style.AppTheme_LIGHTGREEN_NoActionBar, false));
            themeDao.insert(new Theme("LIME", R.style.AppTheme_LIME, R.style.AppTheme_LIME_NoActionBar, false));
            themeDao.insert(new Theme("YELLOW", R.style.AppTheme_YELLOW, R.style.AppTheme_YELLOW_NoActionBar, false));
            themeDao.insert(new Theme("AMBER", R.style.AppTheme_AMBER, R.style.AppTheme_AMBER_NoActionBar, false));
            themeDao.insert(new Theme("ORANGE", R.style.AppTheme_ORANGE, R.style.AppTheme_ORANGE_NoActionBar, false));
            themeDao.insert(new Theme("DEEPORANGE", R.style.AppTheme_DEEPORANGE, R.style.AppTheme_DEEPORANGE_NoActionBar, false));
            themeDao.insert(new Theme("BROWN", R.style.AppTheme_BROWN, R.style.AppTheme_BROWN_NoActionBar, false));
            themeDao.insert(new Theme("GRAY", R.style.AppTheme_GRAY, R.style.AppTheme_GRAY_NoActionBar, false));
            themeDao.insert(new Theme("BLUEGRAY", R.style.AppTheme_BLUEGRAY, R.style.AppTheme_BLUEGRAY_NoActionBar, false));
            return null;
        }
    }

}
