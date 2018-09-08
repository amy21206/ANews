package com.java.wuzihan.anews.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.java.wuzihan.anews.ANewsRepository;
import com.java.wuzihan.anews.database.entity.News;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {

    private ANewsRepository mRepository;

    private MutableLiveData<String> searchPrompt;
    private LiveData<List<News>> newsList;

    public SearchViewModel(Application application) {
        super(application);
        this.searchPrompt = new MutableLiveData<>();
        setSearchCategory("somestrangevaluetomakesurethere'snoresultalsdfdjfas");
        mRepository = new ANewsRepository(application);
        newsList = Transformations.switchMap(searchPrompt, prompt -> mRepository.getSearchResult(prompt));
    }

    public void setSearchCategory(String prompt) {
        this.searchPrompt.setValue(prompt);
    }

    public LiveData<List<News>> getNewsList() {
        return newsList;
    }
}
