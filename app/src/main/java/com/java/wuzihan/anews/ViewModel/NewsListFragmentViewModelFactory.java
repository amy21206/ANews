package com.java.wuzihan.anews.ViewModel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.java.wuzihan.anews.database.entity.Category;

public class NewsListFragmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;
    private String mCategory;

    public NewsListFragmentViewModelFactory(Application application, String category) {
        mApplication = application;
        mCategory = category;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new NewsListFragmentViewModel(mApplication, mCategory);
    }
}
