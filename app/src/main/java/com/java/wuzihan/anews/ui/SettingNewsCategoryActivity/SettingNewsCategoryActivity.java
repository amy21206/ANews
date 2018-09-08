package com.java.wuzihan.anews.ui.SettingNewsCategoryActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.java.wuzihan.anews.R;
import com.java.wuzihan.anews.ViewModel.SettingNewsCategoryViewModel;
import com.java.wuzihan.anews.database.entity.Category;

import java.util.ArrayList;
import java.util.List;


public class SettingNewsCategoryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CategoryItemsAdapter mAdapter;
    private SettingNewsCategoryViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_news_category);
        mRecyclerView = findViewById(R.id.recyclerview_settings_news_list);
        mViewModel = ViewModelProviders.of(this).get(SettingNewsCategoryViewModel.class);

        final List<Category> categoryList = new ArrayList<>();
        final Observer<List<Category>> categoryObserver = new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> newsCategories) {
                categoryList.clear();
                categoryList.addAll(newsCategories);
                mRecyclerView = findViewById(R.id.recyclerview_settings_news_list);
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        };
        mViewModel.getAllCategories().observe(this, categoryObserver);
        mAdapter = new CategoryItemsAdapter(this.getApplicationContext(), categoryList, mViewModel);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
    }
}

class CategoryItemsAdapter extends RecyclerView.Adapter<CategoryItemsAdapter.CategoryItemHolder> {

    private final List<Category> mCategoryList;
    private LayoutInflater mInflater;
    private SettingNewsCategoryViewModel mViewModel;

    public CategoryItemsAdapter(Context context, List<Category> categoryList, SettingNewsCategoryViewModel viewModel) {
        mInflater = LayoutInflater.from(context);
        mCategoryList = categoryList;
        mViewModel = viewModel;
    }

    @Override
    public CategoryItemsAdapter.CategoryItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.item_settings_news_category, parent, false);
        CategoryItemHolder holder = new CategoryItemHolder(mItemView, this);
        return holder;
    }

    @Override
    public void onBindViewHolder(CategoryItemsAdapter.CategoryItemHolder holder, int position) {
        holder.bind(mCategoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public void updateCategory(String categoryName, boolean categoryShown) {
        mViewModel.updateCategory(categoryName, categoryShown);
    }

    class CategoryItemHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        final CategoryItemsAdapter mAdapter;

        public CategoryItemHolder(View view, CategoryItemsAdapter adapter) {
            super(view);
            mAdapter = adapter;
            checkBox = view.findViewById(R.id.checkBox_setting_news_category);
            this.setIsRecyclable(false);
        }

        public void bind(final Category item) {
            checkBox.setChecked(item.isShown());
            checkBox.setText(item.getName());
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    new updateCategoryAsyncTask(mAdapter, checkBox.isChecked()).execute((String) checkBox.getText());
                }
            });
        }

        private class updateCategoryAsyncTask extends AsyncTask<String, Void, Void> {

            private CategoryItemsAdapter mAdapter;
            private boolean mCategoryShown;

            updateCategoryAsyncTask(CategoryItemsAdapter adapter, boolean categoryShown) {
                mAdapter = adapter;
                mCategoryShown = categoryShown;
            }

            @Override
            protected Void doInBackground(final String... params) {
                mAdapter.updateCategory(params[0], mCategoryShown);
                return null;
            }
        }
    }
}
