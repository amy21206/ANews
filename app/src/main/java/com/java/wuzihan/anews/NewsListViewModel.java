package com.java.wuzihan.anews;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.java.wuzihan.anews.database.News;

import java.util.HashMap;
import java.util.List;

public class NewsListViewModel extends ViewModel {

    private NewsListModel model_;
    private MutableLiveData<List<String>> newsCategories;
    private MutableLiveData<HashMap<String, List<News>>> categoryToNewsList;

    public NewsListViewModel() {
        model_ = new NewsListModel();
    }

    public LiveData<List<String>> getNewsCategories() {
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

    public LiveData<HashMap<String, List<News>>> getCategoryToNewsList() {
        if (categoryToNewsList == null) {
            categoryToNewsList = new MutableLiveData<>();
            HashMap<String, List<News>> init = new HashMap<>();
            categoryToNewsList.setValue(init);
        }
        return categoryToNewsList;
    }

    public void updateCategory(String category) {
        if (categoryToNewsList == null) {
            categoryToNewsList = new MutableLiveData<>();
            HashMap<String, List<News>> init = new HashMap<>();
            categoryToNewsList.setValue(init);
        }
        // TODO: replace hardcoded stuff.
        Log.d("NewsListViewModel", "fetching News List");
        if (categoryToNewsList.getValue().containsKey(category)) {
            return;
        }
        model_.updateCategory(category, categoryToNewsList);
    }
}
