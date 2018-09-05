package com.java.wuzihan.anews.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.java.wuzihan.anews.ANewsRepository;
import com.java.wuzihan.anews.database.entity.Category;
import com.java.wuzihan.anews.database.entity.News;
import com.java.wuzihan.anews.ui.NewsListActivity.NewsListModel;

import java.util.HashMap;
import java.util.List;

public class NewsListViewModel extends AndroidViewModel {

    private ANewsRepository mRepository;

    private NewsListModel model_;
    private LiveData<List<Category>> newsCategories;
    private MutableLiveData<HashMap<String, List<News>>> categoryToNewsList;

    public NewsListViewModel(Application application) {
        super(application);
        mRepository = new ANewsRepository(application);
        model_ = new NewsListModel();
        newsCategories = mRepository.getShownCategories();
    }

    public LiveData<List<Category>> getShownCategories() {
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
