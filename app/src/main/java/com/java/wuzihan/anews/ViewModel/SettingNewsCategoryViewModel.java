package com.java.wuzihan.anews.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.java.wuzihan.anews.ANewsRepository;
import com.java.wuzihan.anews.database.entity.Category;
import com.java.wuzihan.anews.database.entity.Theme;

import java.util.List;

public class SettingNewsCategoryViewModel extends AndroidViewModel {

    private ANewsRepository mRepository;

    private LiveData<List<Category>> allCategories;
    private Theme theme;

    public SettingNewsCategoryViewModel(Application application) {
        super(application);
        mRepository = new ANewsRepository(application);
        allCategories = mRepository.getAllCategories();
        theme = mRepository.getTheme();
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public void updateCategory(String categoryName, boolean categoryShown) {
        mRepository.updateCategory(categoryName, categoryShown);
    }

    public Theme getTheme() {
        return theme;
    }
}
