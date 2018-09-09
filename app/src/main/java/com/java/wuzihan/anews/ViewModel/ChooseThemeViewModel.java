package com.java.wuzihan.anews.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.java.wuzihan.anews.ANewsRepository;
import com.java.wuzihan.anews.database.entity.Theme;

import java.util.List;

public class ChooseThemeViewModel extends AndroidViewModel {
    private ANewsRepository mRepository;
    private LiveData<List<Theme>> themeList;
    private Theme theme;
    private Theme selectedTheme = null;

    public ChooseThemeViewModel(Application application) {
        super(application);
        mRepository = new ANewsRepository(application);
        theme = mRepository.getTheme();
        themeList = mRepository.getAllThemes();
    }

    public LiveData<List<Theme>> getThemeList() {
        return themeList;
    }

    public Theme getTheme() {
        return mRepository.getTheme();
    }

    public void setSelectedTheme(Theme selectedTheme) {
        this.selectedTheme = selectedTheme;
    }

    public void updateTheme() {
        if (selectedTheme == null || selectedTheme.getName().equals(theme.getName())) return;
        mRepository.setTheme(selectedTheme);
    }

    public Theme getSelectedTheme() {
        return selectedTheme;
    }
}
