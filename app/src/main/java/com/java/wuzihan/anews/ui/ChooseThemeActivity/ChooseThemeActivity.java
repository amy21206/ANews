package com.java.wuzihan.anews.ui.ChooseThemeActivity;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.java.wuzihan.anews.R;
import com.java.wuzihan.anews.ViewModel.ChooseThemeViewModel;
import com.java.wuzihan.anews.database.entity.Theme;

import java.util.ArrayList;
import java.util.List;

public class ChooseThemeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ChooseThemeAdapter adapter;
    ChooseThemeViewModel mViewModel;
    Theme curTheme;
    private final int REQUEST_FOR_THEME = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChooseThemeViewModel.class);
        curTheme = mViewModel.getTheme();
        setTheme(curTheme.getAppBarId());
        setContentView(R.layout.activity_choose_theme);
        recyclerView = findViewById(R.id.recyclerview_theme);
        final List<Theme> themeList = new ArrayList<>();
        adapter = new ChooseThemeAdapter(this.getApplicationContext(), themeList, curTheme, mViewModel);


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));

        final Observer<List<Theme>> newsObserver = new Observer<List<Theme>>() {
            @Override
            public void onChanged(List<Theme> favoriteTheme) {
                themeList.clear();
                themeList.addAll(favoriteTheme);
                recyclerView = findViewById(R.id.recyclerview_theme);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        };
        mViewModel.getThemeList().observe(this, newsObserver);
    }

    public void onClick(View view) {
        if (mViewModel.getSelectedTheme() == null) {
            Toast.makeText(this, "请选择主题",
                    Toast.LENGTH_SHORT).show();
        } else {
            mViewModel.updateTheme();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("THEME_ID", mViewModel.getSelectedTheme().getNoAppBarId());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}
