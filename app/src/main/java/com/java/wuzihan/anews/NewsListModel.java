package com.java.wuzihan.anews;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.util.ArrayList;
import java.util.List;

public class NewsListModel {

    private List<String> mNewsCategories;

    public NewsListModel() {
        mNewsCategories = new ArrayList<>();
    }

    @Nullable
    public MutableLiveData<List<String>> getNewsCategories() {
        // TODO: replace hardcoded rss for maps with category for now
        //String urlString = "http://news.qq.com/newsgn/rss_newsgn.xml";
        mNewsCategories.add("HAhahaha");
        mNewsCategories.add("wuwuwuwu");
        final MutableLiveData<List<String>> newsCategories = new MutableLiveData<>();
        newsCategories.setValue(mNewsCategories);
        return newsCategories;
    }
}
