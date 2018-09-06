package com.java.wuzihan.anews.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.java.wuzihan.anews.ANewsRepository;
import com.java.wuzihan.anews.database.entity.Category;
import com.java.wuzihan.anews.database.entity.News;
import com.java.wuzihan.anews.ui.NewsListActivity.NewsListFragment;

import java.util.List;

public class NewsListFragmentViewModel extends AndroidViewModel {

    private ANewsRepository mRepository;
    private String category;
    private LiveData<List<News>> newsList;

    public NewsListFragmentViewModel(Application application, String category) {
        super(application);
        mRepository = new ANewsRepository(application);
        this.category = category;
        newsList = mRepository.getNewsListByCategoryName(this.category);
    }

    public LiveData<List<News>> getNews() {
        return newsList;
    }
}

