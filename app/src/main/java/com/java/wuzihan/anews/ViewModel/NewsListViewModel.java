package com.java.wuzihan.anews.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.java.wuzihan.anews.ANewsRepository;
import com.java.wuzihan.anews.database.entity.Category;
import com.java.wuzihan.anews.database.entity.News;

import java.util.HashMap;
import java.util.List;

public class NewsListViewModel extends AndroidViewModel {

    private ANewsRepository mRepository;

    private LiveData<List<Category>> newsCategories;
    private MutableLiveData<HashMap<String, List<News>>> categoryToNewsList;

    public NewsListViewModel(Application application) {
        super(application);
        mRepository = new ANewsRepository(application);
        newsCategories = mRepository.getShownCategories();
    }

    public LiveData<List<Category>> getShownCategories() {
        return newsCategories;
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
    }
}
