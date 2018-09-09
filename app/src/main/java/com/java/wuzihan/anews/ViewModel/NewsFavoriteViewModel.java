package com.java.wuzihan.anews.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.java.wuzihan.anews.ANewsRepository;
import com.java.wuzihan.anews.database.entity.News;
import com.java.wuzihan.anews.database.entity.Theme;

import java.util.List;

public class NewsFavoriteViewModel extends AndroidViewModel {

    private ANewsRepository mRepository;
    private Theme theme;
    private LiveData<List<News>> favoriteNews;

    public NewsFavoriteViewModel(Application application) {
        super(application);
        mRepository = new ANewsRepository(application);
        favoriteNews = mRepository.getFavoriteNews();
        theme = mRepository.getTheme();
    }

    public LiveData<List<News>> getFavoriteNews() {
        return favoriteNews;
    }

    public Theme getTheme() {
        return theme;
    }
}
