package com.java.wuzihan.anews;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.java.wuzihan.anews.database.entity.News;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsListModel {

    private List<String> mNewsCategories;
    private tempData mData;

    public NewsListModel() {
        mNewsCategories = new ArrayList<>();
        mData = new tempData();
        for (String category: mData.getmCategoryToUrl().keySet()) {
            mNewsCategories.add(category);
        }
    }

    @Nullable
    public MutableLiveData<List<String>> getNewsCategories() {
        // TODO: replace hardcoded rss for maps with category for now
        Log.d("Model", "getNewsCategories");
        final MutableLiveData<List<String>> newsCategories = new MutableLiveData<>();
        newsCategories.setValue(mNewsCategories);
        return newsCategories;
    }

    public MutableLiveData<List<News>> getNewsList(String category) {
        Log.d("Model", "getNewsList");
        final MutableLiveData<List<News>> newsListData = new MutableLiveData<>();
        if (mData.getmCategoryToNews().containsKey(category)) {
            newsListData.setValue(mData.getmCategoryToNews().get(category));
            Log.d("Model", "existNewsList");
        } else {
            List<News> newsList = new ArrayList<>();
            Log.d("Model", "fetchNewsList");
//            NewsFetchThread myThread = new NewsFetchThread("fetchNewsThread", mData.getmCategoryToUrl().get(category), newsList, newsListData);
//            myThread.start();
        }
        return newsListData;
    }

    public void updateCategory(String category, MutableLiveData<HashMap<String,List<News>>> data) {
        NewsFetchThread myThread = new NewsFetchThread("fetchNewsThread", mData.getmCategoryToUrl().get(category), category, data);
        myThread.start();
    }
}

class tempData {
    private HashMap<String, String> mCategoryToUrl;
    private HashMap<String, List<News>> mCategoryToNews;
    tempData() {
        mCategoryToUrl = new HashMap<>();
        mCategoryToNews = new HashMap<>();
        mCategoryToUrl.put("国内", "http://news.qq.com/newsgn/rss_newsgn.xml");
        mCategoryToUrl.put("娱乐", "http://ent.qq.com/movie/jvrss_movie.xml");
        mCategoryToUrl.put("财经", "http://finance.qq.com/financenews/breaknews/rss_finance.xml");
        mCategoryToUrl.put("科技", "http://tech.qq.com/web/rss_web.xml");
        mCategoryToUrl.put("体育", "http://sports.qq.com/rss_newssports.xml");
        mCategoryToUrl.put("国际", "http://news.qq.com/newsgj/rss_newswj.xml");
        mCategoryToUrl.put("游戏", "http://games.qq.com/ntgame/rss_ntgame.xml");
        mCategoryToUrl.put("教育", "http://edu.qq.com/gaokao/rss_gaokao.xml");
        mCategoryToUrl.put("动漫", "http://comic.qq.com/news/rss_news.xml");
        mCategoryToUrl.put("时尚", "http://luxury.qq.com/staff/rss_staff.xml");
        mCategoryToUrl.put("人物", "http://news.qq.com/person/rss_person.xml");
    }

    public HashMap<String, String> getmCategoryToUrl() {
        return mCategoryToUrl;
    }

    public HashMap<String, List<News>> getmCategoryToNews() {
        return mCategoryToNews;
    }
}



class NewsFetchThread extends Thread {

    String mUrl;
    List<News> mNewsList;
    String mCategory;
    MutableLiveData<HashMap<String,List<News>>> mCategoryToNewsListData;

    NewsFetchThread(String name, String url, String category, MutableLiveData<HashMap<String, List<News>>> categoryToNewsListData) {
        super(name);
        mUrl = url;
        mCategory = category;
        mNewsList = new ArrayList<>();
        mCategoryToNewsListData = categoryToNewsListData;
    }

    @Override
    public void run() {
        super.run();
        Log.d("NewsListModel","run");
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
                    System.out.println(title);
                    System.out.println(link);
                    System.out.println(pubDate);
                    System.out.println(description);
                    News piece = new News(mCategory, title, description, link, pubDate);
                    mNewsList.add(piece);
                } catch (Exception e) {}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("NewsListModel", String.valueOf(mNewsList.size()));
        HashMap<String, List<News>> map = mCategoryToNewsListData.getValue();
        map.put(mCategory, mNewsList);
        mCategoryToNewsListData.postValue(map);
    }
}

