package com.java.wuzihan.anews;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.java.wuzihan.anews.database.ANewsDatabase;
import com.java.wuzihan.anews.database.dao.CategoryDao;
import com.java.wuzihan.anews.database.entity.Category;

import java.util.List;

public class ANewsRepository {

    private CategoryDao mCategoryDao;
    private LiveData<List<Category>> mAllCategories;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public ANewsRepository(Application application) {
        ANewsDatabase db = ANewsDatabase.getDatabase(application);
        mCategoryDao = db.categoryDao();
        mAllCategories = mCategoryDao.getAllCategories();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Category>> getAllCategories() {
        return mAllCategories;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void insert(Category category) {
        new insertAsyncTask(mCategoryDao).execute(category);
    }

    private static class insertAsyncTask extends AsyncTask<Category, Void, Void> {

        private CategoryDao mAsyncTaskDao;

        insertAsyncTask(CategoryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Category... params) {
            mAsyncTaskDao.insertCategory(params[0]);
            return null;
        }
    }
}
