package com.java.wuzihan.anews.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.java.wuzihan.anews.ANewsRepository;

public class NewsDetailsViewModel extends AndroidViewModel {

    ANewsRepository mRepository;

    public NewsDetailsViewModel(Application application) {
        super(application);
        mRepository = new ANewsRepository(application);
    }

    public void setNewsFavorite(String newsTitle, boolean favorite) {
        mRepository.setNewsFavorite(newsTitle, favorite);
    }
}
