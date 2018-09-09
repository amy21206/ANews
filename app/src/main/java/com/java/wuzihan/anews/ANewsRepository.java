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
import com.java.wuzihan.anews.database.dao.ThemeDao;
import com.java.wuzihan.anews.database.entity.Category;
import com.java.wuzihan.anews.database.entity.News;
import com.java.wuzihan.anews.database.entity.Theme;

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
    private ThemeDao themeDao;
    private HashMap<String, LiveData<List<News>>> mCategoryToNews;
    private LiveData<List<Category>> mAllCategories;
    private LiveData<List<Category>> mShownCategories;
    private LiveData<List<News>> mFavoriteNews;

    private LiveData<List<News>> mSearchResult;
    private LiveData<List<News>> mRecommendNews;
    private LiveData<List<Category>> mMostViewedCategory;
    private LiveData<List<Theme>> allThemes;


    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public ANewsRepository(Application application) {
        ANewsDatabase db = ANewsDatabase.getDatabase(application);
        mCategoryDao = db.categoryDao();
        mNewsDao = db.newsDao();
        themeDao = db.themeDao();
        mAllCategories = mCategoryDao.getAllCategories();
        mShownCategories = mCategoryDao.getShownCategories();
        mCategoryToNews = new HashMap<>();
        mFavoriteNews = mNewsDao.getNewsFavorite();
        mSearchResult = mNewsDao.getNewsSearchBy("alsdjfa;lifja;wiefja;weifajwe;if");
        Log.d("search", mSearchResult.toString());
        mMostViewedCategory = mCategoryDao.getMostViewedThreeCategory();
        mRecommendNews = new MutableLiveData<>();
        allThemes = themeDao.getThemes();
//        new UpdateRecommendAsyncTask(mNewsDao, mRecommendNews).execute(mMostViewedCategory.getValue());
    }

    public LiveData<List<Category>> getAllCategories() {
        return mAllCategories;
    }

    public LiveData<List<Category>> getShownCategories() {
        return mShownCategories;
    }

    public LiveData<List<News>> getFavoriteNews() {
        return mFavoriteNews;
    }

    public LiveData<List<Category>> getMostViewedCategory() {
        return mMostViewedCategory;
    }

    public LiveData<List<News>> getSearchResult(String prompt) {
        mSearchResult = mNewsDao.getNewsSearchBy(prompt);
        Log.d("search", "getsearchresult");
        return mSearchResult;
    }

    public LiveData<List<News>> getRecommendNews(List<Category> cats) {
        mRecommendNews = mNewsDao.getNewsRecommend(cats.get(0).getName(), cats.get(1).getName(), cats.get(2).getName(), 10);
        return mRecommendNews;
    }

    public Theme getTheme() {
        return themeDao.selectedTheme();
    }

    public LiveData<List<Theme>> getAllThemes() {
        return allThemes;
    }

    public LiveData<List<News>> getNewsListByCategoryName(String categoryName) {
        if (!mCategoryToNews.containsKey(categoryName)) {
            mCategoryToNews.put(categoryName, mNewsDao.getNewsOfCategoryByNumber(categoryName, 5));
            updateNewsListByCategoryName(categoryName);
        }
        return mCategoryToNews.get(categoryName);
    }

    public void updateCategory(String categoryName, boolean categoryShown) {
        new updateCategoryAsyncTask(mCategoryDao, categoryShown).execute(categoryName);
    }

    private void updateNewsListByCategoryName(String categoryName) {
        NewsFetchThread thread = new NewsFetchThread("newsFetchThread", mNewsDao, mCategoryDao, categoryName);
        thread.start();
    }

    public void setNewsFavorite(String newsTitle, boolean favorite) {
        Log.d("favorite", "repo set");
        new setNewsFavoriteAsyncTask(mNewsDao, newsTitle).execute(favorite);
    }

    public void setNewsViewed(String newsTitle, boolean viewed) {
        new setNewsViewedAsyncTask(mNewsDao, mCategoryDao, newsTitle).execute(viewed);
//        new UpdateRecommendAsyncTask(mNewsDao, mCategoryDao, mRecommendNews).execute(mMostViewedCategory);
    }

    public List<News> getNewsMore(String catTitle, int start, int end) {
        return mNewsDao.getNewsOfCategoryByNumberAndOffsetNotLive(catTitle, end - start, start);
    }

    public void fetchAllData() {
        List<Category> allCats = mCategoryDao.getAllCategories().getValue();
        for (Category cat : allCats) {
            NewsFetchThread thread = new NewsFetchThread(cat.getName(), mNewsDao, mCategoryDao, cat.getName());
        }
    }

    public void refresh() {
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
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    private static class setNewsViewedAsyncTask extends AsyncTask<Boolean, Void, Void> {

        private NewsDao mNewsDao;
        private CategoryDao mCategoryDao;
        private String mNewsTitle;

        setNewsViewedAsyncTask(NewsDao dao, CategoryDao cdao, String newsTitle) {
            mNewsDao = dao;
            mCategoryDao = cdao;
            mNewsTitle = newsTitle;
        }

        @Override
        protected Void doInBackground(final Boolean... params) {
            mNewsDao.updateNewsViewed(mNewsTitle, params[0]);
            String cat = mNewsDao.getNewsByHeading(mNewsTitle).getCategory();
            Category category = mCategoryDao.getCategoryByName(cat);
            mCategoryDao.updateCategoryReadTimes(cat, category.getReadtime() + 1);
            return null;
        }
    }

    private static class UpdateRecommendAsyncTask extends AsyncTask<LiveData<List<Category>>, Void, Void> {
        private NewsDao mNewsDao;
        private CategoryDao categoryDao;
        private MutableLiveData<List<News>> newsList;

        UpdateRecommendAsyncTask(NewsDao dao, CategoryDao categoryDao, MutableLiveData<List<News>> newsList) {
            mNewsDao = dao;
            this.newsList = newsList;
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(final LiveData<List<Category>>... params) {
            List<Category> catList = categoryDao.getMostViewedThreeCategory().getValue();
            List<Integer> weight = new ArrayList<>(3);
            weight.add(5);
            weight.add(3);
            weight.add(2);
            List<News> mNewsList = new ArrayList<>();
            for (int i = 0; i < weight.size(); ++i) {
                String cat = catList.get(i).getName();
                mNewsList.addAll(mNewsDao.getNewsOfCategoryByNumber(cat, weight.get(i)).getValue());
            }
            newsList.setValue(mNewsList);
            return null;
        }
    }

    public void setTheme(Theme theme) {
        Theme currentTheme = themeDao.selectedTheme();
        themeDao.updateThemeChose(currentTheme.getName(), false);
        themeDao.updateThemeChose(theme.getName(), true);
    }
}
