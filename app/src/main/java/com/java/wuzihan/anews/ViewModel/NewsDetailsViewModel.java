package com.java.wuzihan.anews.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.util.Log;

import com.java.wuzihan.anews.ANewsRepository;
import com.java.wuzihan.anews.database.entity.Theme;

public class NewsDetailsViewModel extends AndroidViewModel {

    private ANewsRepository mRepository;
    private Theme theme;

    public NewsDetailsViewModel(Application application) {
        super(application);
        mRepository = new ANewsRepository(application);
        theme = mRepository.getTheme();
    }

    public void setNewsFavorite(String newsTitle, boolean favorite) {
        mRepository.setNewsFavorite(newsTitle, favorite);
        Log.d("favorite", "viewmodel set");
    }

    public Theme getTheme() {
        return theme;
    }
}
