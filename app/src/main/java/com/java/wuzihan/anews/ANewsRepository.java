package com.java.wuzihan.anews;

import android.app.Application;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.java.wuzihan.anews.database.ANewsDatabase;
import com.java.wuzihan.anews.database.dao.CategoryDao;
import com.java.wuzihan.anews.database.dao.NewsDao;
import com.java.wuzihan.anews.database.entity.Category;
import com.java.wuzihan.anews.database.entity.News;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ANewsRepository {

    private CategoryDao mCategoryDao;
    private NewsDao mNewsDao;
    private HashMap<String, LiveData<List<News>>> mCategoryToNews;
    private LiveData<List<Category>> mAllCategories;
    private LiveData<List<Category>> mShownCategories;
    private LiveData<List<News>> mFavoriteNews;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public ANewsRepository(Application application) {
        ANewsDatabase db = ANewsDatabase.getDatabase(application);
        mCategoryDao = db.categoryDao();
        mNewsDao = db.newsDao();
        mAllCategories = mCategoryDao.getAllCategories();
        mShownCategories = mCategoryDao.getShownCategories();
        mCategoryToNews = new HashMap<>();
        mFavoriteNews = mNewsDao.getNewsFavorite();
    }

    public void updateCategory(String categoryName, boolean categoryShown) {
        new updateCategoryAsyncTask(mCategoryDao, categoryShown).execute(categoryName);
    }

    private static class updateCategoryAsyncTask extends AsyncTask<String, Void, Void> {

        private CategoryDao mAsyncTaskDao;
        private boolean mCategoryShown;

        updateCategoryAsyncTask(CategoryDao dao, boolean categoryShown) {
            mAsyncTaskDao = dao;
            mCategoryShown = categoryShown;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.updateCategory(params[0], mCategoryShown);

            return null;
        }
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Category>> getAllCategories() {
        return mAllCategories;
    }

    public LiveData<List<Category>> getShownCategories() {
        return mShownCategories;
    }

    public LiveData<List<News>> getFavoriteNews() {
        return mFavoriteNews;
    }

    public LiveData<List<News>> getNewsListByCategoryName(String categoryName) {
        if (!mCategoryToNews.containsKey(categoryName)) {
            mCategoryToNews.put(categoryName, mNewsDao.getNewsOfCategory(categoryName));
            updateNewsListByCategoryName(categoryName);
        }
        return mCategoryToNews.get(categoryName);
    }

    // gets data from urls.
    private void updateNewsListByCategory(Category category) {
//        NewsFetchThread thread = new NewsFetchThread("newsFetchThread", mNewsDao, category);
//        thread.start();
    }

    public void updateNewsListByCategoryName(String categoryName) {
        NewsFetchThread thread = new NewsFetchThread("newsFetchThread", mNewsDao, mCategoryDao, categoryName);
        thread.start();
    }

    private static class NewsFetchThread extends Thread {

        private final String categoryName;
        private final NewsDao mNewsDao;
        private final CategoryDao mCategoryDao;

        NewsFetchThread(String name, NewsDao mNewsDao, CategoryDao mCategoryDao, String categoryName) {
            super(name);
            this.mNewsDao = mNewsDao;
            this.mCategoryDao = mCategoryDao;
            this.categoryName = categoryName;
        }

        @Override
        public void run() {
            super.run();
            Log.d("fetching data", categoryName);
            Category category = mCategoryDao.getCategoryByName(categoryName);
            String mUrl = category.getUrl();
            String mCategory = category.getName();
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
                        Log.d("insert", mCategory);
                        Log.d("insert", "insert");
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setNewsFavorite(String newsTitle, boolean favorite) {
        Log.d("favorite", "repo set");
        new setNewsFavoriteAsyncTask(mNewsDao, newsTitle).execute(favorite);
    }

    private static class setNewsFavoriteAsyncTask extends AsyncTask<Boolean, Void, Void> {

        private NewsDao mNewsDao;
        private String mNewsTitle;

        setNewsFavoriteAsyncTask(NewsDao dao, String newsTitle) {
            mNewsDao = dao;
            mNewsTitle = newsTitle;
        }

        @Override
        protected Void doInBackground(final Boolean... params) {
            mNewsDao.updateNewsFavorite(mNewsTitle, params[0]);
            Log.d("favorite", "doInbackground");
            return null;
        }
    }
}
