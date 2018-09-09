package com.java.wuzihan.anews.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.util.Log;

import com.java.wuzihan.anews.ANewsRepository;
import com.java.wuzihan.anews.database.entity.Theme;
import com.java.wuzihan.anews.ui.MainActivity.MainActivity;

public class MainActivityViewModel extends AndroidViewModel {

    private ANewsRepository mRepository;
    private Theme theme;

    public MainActivityViewModel(Application application) {
        super(application);
        mRepository = new ANewsRepository(application);
        theme = mRepository.getTheme();
    }

    public void FetchAllData() {
        mRepository.fetchAllData();
    }

    public Theme getTheme() {
        return theme;
    }
}
