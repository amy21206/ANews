package com.java.wuzihan.anews;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

public class NewsListViewModel extends ViewModel {

    private NewsListModel model_;
    private MutableLiveData<List<String>> newsCategories;

    public NewsListViewModel() {
        model_ = new NewsListModel();
    }

    public LiveData<List<String>> getNewsList() {
        Log.d("NewsListViewModel", "getting News List");
        if (newsCategories == null) {
            newsCategories = mFetchNewsCategories();
        }
        return newsCategories;
    }

    private MutableLiveData<List<String>> mFetchNewsCategories() {
        // TODO: replace hardcoded stuff.
        Log.d("NewsListViewModel", "fetching News");
        return model_.getNewsCategories();
    }


}
