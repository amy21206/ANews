package com.java.wuzihan.anews.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.java.wuzihan.anews.ANewsRepository;
import com.java.wuzihan.anews.database.entity.Category;

import java.util.List;

public class SettingNewsCategoryViewModel extends AndroidViewModel {

    private ANewsRepository mRepository;

    private LiveData<List<Category>> allCategories;

    public SettingNewsCategoryViewModel(Application application) {
        super(application);
        mRepository = new ANewsRepository(application);
        allCategories = mRepository.getAllCategories();
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }
}
