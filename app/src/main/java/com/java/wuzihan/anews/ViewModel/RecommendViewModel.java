package com.java.wuzihan.anews.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.java.wuzihan.anews.ANewsRepository;
import com.java.wuzihan.anews.database.entity.News;

import java.util.List;

public class RecommendViewModel extends AndroidViewModel {

    private ANewsRepository mRepository;

    private LiveData<List<News>> newsList;

    public RecommendViewModel(Application application) {
        super(application);
        mRepository = new ANewsRepository(application);
    }

    public LiveData<List<News>> getNewsList() {
        return newsList;
    }
}
