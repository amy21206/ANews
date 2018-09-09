package com.java.wuzihan.anews.ui.MainActivity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.java.wuzihan.anews.R;
import com.java.wuzihan.anews.ViewModel.MainActivityViewModel;
import com.java.wuzihan.anews.ui.NewsListActivity.NewsListActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivityViewModel mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
//        mViewModel.FetchAllData();

        startActivity(new Intent(this, NewsListActivity.class));
        finish();
    }
}
