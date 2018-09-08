package com.java.wuzihan.anews.ui.NewsListActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.java.wuzihan.anews.R;
import com.java.wuzihan.anews.database.entity.Category;
import com.java.wuzihan.anews.ViewModel.NewsListViewModel;
import com.java.wuzihan.anews.ui.NewsFavoriteActivity.NewsFavoriteActivity;
import com.java.wuzihan.anews.ui.SettingNewsCategoryActivity.SettingNewsCategoryActivity;

import java.util.ArrayList;
import java.util.List;

public class NewsListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NewsListViewModel mViewModel;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private NewsListTabAdapter mTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        // Setting toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setting drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Setting navigation view
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Setting tab adapter
        mViewPager = findViewById(R.id.newsListViewPager);
        mTabLayout = findViewById(R.id.newsListTabLayout);
        mTabAdapter = new NewsListTabAdapter(getSupportFragmentManager());
        // Setting ViewModel
        mViewModel = ViewModelProviders.of(this).get(NewsListViewModel.class);
        final Observer<List<Category>> articleObserver = new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                List<String> categoryTitles = new ArrayList<>();
                for (Category category : categories) {
                    categoryTitles.add(category.getName());
                }
                mTabAdapter.setFragmentTitleList(categoryTitles);
                mTabAdapter.notifyDataSetChanged();
            }
        };
        mViewModel.getShownCategories().observe(this, articleObserver);
        mViewPager.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(NewsListActivity.this, SettingNewsCategoryActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favorite) {
            Intent intent = new Intent();
            intent.setClass(NewsListActivity.this, NewsFavoriteActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
