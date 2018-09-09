package com.java.wuzihan.anews.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.java.wuzihan.anews.ANewsRepository;
import com.java.wuzihan.anews.database.entity.Category;
import com.java.wuzihan.anews.database.entity.News;
import com.java.wuzihan.anews.database.entity.Theme;
import com.java.wuzihan.anews.ui.NewsListActivity.NewsListFragment;

import java.util.List;

public class NewsListFragmentViewModel extends AndroidViewModel {

    private ANewsRepository mRepository;
    private String category;
    private LiveData<List<News>> newsList;
    private List<News> newsListMore;
    private Theme theme;

    public NewsListFragmentViewModel(Application application, String category) {
        super(application);
        mRepository = new ANewsRepository(application);
        this.category = category;
        newsList = mRepository.getNewsListByCategoryName(this.category);
        theme = mRepository.getTheme();
    }

    public LiveData<List<News>> getNews() {
        return newsList;
    }

    public void setNewsViewed(String newsTitle, boolean viewed) {
        mRepository.setNewsViewed(newsTitle, viewed);
    }

    public List<News> getNewsMore(int start, int end) {
        return mRepository.getNewsMore(this.category, start, end);
    }

    public Theme getTheme() {
        return mRepository.getTheme();
    }


    public void refresh() {
        mRepository.refresh();
    }
}

