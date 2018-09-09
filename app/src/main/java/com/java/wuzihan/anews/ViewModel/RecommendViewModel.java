package com.java.wuzihan.anews.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.java.wuzihan.anews.ANewsRepository;
import com.java.wuzihan.anews.database.entity.Category;
import com.java.wuzihan.anews.database.entity.News;
import com.java.wuzihan.anews.database.entity.Theme;

import java.util.List;

public class RecommendViewModel extends AndroidViewModel {

    private ANewsRepository mRepository;

    private Theme theme;
    private LiveData<List<Category>> topCats;
    private LiveData<List<News>> newsList;

    public RecommendViewModel(Application application) {
        super(application);
        mRepository = new ANewsRepository(application);
        topCats = mRepository.getMostViewedCategory();
        newsList = Transformations.switchMap(topCats, topCats -> mRepository.getRecommendNews(topCats));
        theme = mRepository.getTheme();
    }

    public LiveData<List<News>> getNewsList() {
        return newsList;
    }

    public Theme getTheme() {
        return theme;
    }
}
