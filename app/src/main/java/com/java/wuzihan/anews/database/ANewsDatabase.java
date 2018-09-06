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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            new PopulateDbOnStartAsync(INSTANCE).execute();
        }
    };

    /**
     * Populate the database in the background.
     * If you want to start with more words, just add them.
     */
    private static class PopulateDbOnStartAsync extends AsyncTask<Void, Void, Void> {

        private final CategoryDao categoryDao;
        private final NewsDao newsDao;

        PopulateDbOnStartAsync(ANewsDatabase db) {
            categoryDao = db.categoryDao();
            newsDao = db.newsDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            // populating categories
            categoryDao.insertCategory(new Category("国内", true, "http://news.qq.com/newsgn/rss_newsgn.xml"));
            categoryDao.insertCategory(new Category("娱乐", true, "http://ent.qq.com/movie/jvrss_movie.xml"));
            categoryDao.insertCategory(new Category("财经", true, "http://finance.qq.com/financenews/breaknews/rss_finance.xml"));
            categoryDao.insertCategory(new Category("科技", true, "http://tech.qq.com/web/rss_web.xml"));
            categoryDao.insertCategory(new Category("体育", true, "http://sports.qq.com/rss_newssports.xml"));
            categoryDao.insertCategory(new Category("国际", true, "http://news.qq.com/newsgj/rss_newswj.xml"));
            categoryDao.insertCategory(new Category("游戏", true, "http://games.qq.com/ntgame/rss_ntgame.xml"));
            categoryDao.insertCategory(new Category("教育", true, "http://edu.qq.com/gaokao/rss_gaokao.xml"));
            categoryDao.insertCategory(new Category("动漫", false, "http://comic.qq.com/news/rss_news.xml"));
            categoryDao.insertCategory(new Category("时尚", false, "http://luxury.qq.com/staff/rss_staff.xml"));
            categoryDao.insertCategory(new Category("人物", false, "http://news.qq.com/person/rss_person.xml"));
            Log.d("ANewsDatabase", "inserting");

            // populating news
            /*
            LinkedList<Category> categories = new LinkedList<>();
            categories.addAll(categoryDao.getAllCategories().getValue());
            Collections.synchronizedList(categories);

            try {
                for (int i = 0; i < 5; ++i) {
                    NewsFetchThread thread = new NewsFetchThread("FetchNews", newsDao, categories);
                    thread.start();
                    thread.join();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            return null;
        }
    }

    private static class NewsFetchThread extends Thread {

        private final NewsDao mNewsDao;
        private final LinkedList<Category> mCategoryList;

        NewsFetchThread(String name, NewsDao mNewsDao, LinkedList<Category> categoryList) {
            super(name);
            this.mNewsDao = mNewsDao;
            this.mCategoryList = categoryList;
        }

        @Override
        public void run() {
            super.run();
            while (!mCategoryList.isEmpty()) {
                Category curCategory = mCategoryList.poll();
                String mUrl = curCategory.getUrl();
                String mCategory = curCategory.getName();
                Log.d("NewsListModel", "run");
                try {
                    // TODO: 1. opened it twice to get encoding. change it.
                    // TODO; 2. too long.
                    URL url = new URL(mUrl);
                    BufferedReader inFirstLine = new BufferedReader(new InputStreamReader(url.openStream()));
                    String firstLine = inFirstLine.readLine();
                    Pattern encodingPattern = Pattern.compile("encoding=\".*?\"");
                    Matcher encodingMatcher = encodingPattern.matcher(firstLine);
                    String encoding = encodingMatcher.find() ? firstLine.substring(encodingMatcher.start() + 10, encodingMatcher.end() - 1) : "UTF-8";
                    inFirstLine.close();
                    BufferedReader in =
                            new BufferedReader(new InputStreamReader(url.openStream(), encoding));
                    String xmlContent = "";
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        xmlContent = xmlContent.concat(" " + inputLine);
                    }
                    in.close();

                    Pattern p = Pattern.compile("<item>.*?</item>");
                    Matcher m = p.matcher(xmlContent);
                    while (m.find()) {
                        String item = xmlContent.substring(m.start() + 6, m.end() - 7);
                        Pattern titlePattern = Pattern.compile("<title>.*</title>");
                        Matcher tm = titlePattern.matcher(item);
                        Pattern linkPattern = Pattern.compile("<link>.*?</link>");
                        Matcher lm = linkPattern.matcher(item);
                        Pattern pubDatePattern = Pattern.compile("<pubDate>.*?</pubDate>");
                        Matcher pm = pubDatePattern.matcher(item);
                        Pattern descriptionPattern = Pattern.compile("<description>.*?</description>");
                        Matcher dm = descriptionPattern.matcher(item);
                        tm.find();
                        lm.find();
                        pm.find();
                        dm.find();
                        try {
                            String title = item.substring(tm.start() + 7, tm.end() - 8);
                            String link = item.substring(lm.start() + 6, lm.end() - 7);
                            String pubDate = item.substring(pm.start() + 9, pm.end() - 10);
                            String description = item.substring(dm.start() + 13, dm.end() - 14);
                            News piece = new News(mCategory, title, description, link, pubDate, false, false);
                            mNewsDao.insert(piece);
                        } catch (Exception e) {
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
