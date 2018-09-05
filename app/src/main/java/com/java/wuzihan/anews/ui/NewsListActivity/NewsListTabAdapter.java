package com.java.wuzihan.anews.ui.NewsListActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class NewsListTabAdapter extends FragmentStatePagerAdapter {
    private List<String> mFragmentTitleList;

    NewsListTabAdapter(FragmentManager fm) {
        super(fm);
        mFragmentTitleList = new ArrayList<>();

    }

    public void setFragmentTitleList(List<String> titleList) {
        mFragmentTitleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment newsListFragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("category", mFragmentTitleList.get(position));
        newsListFragment.setArguments(bundle);
        return newsListFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentTitleList.size();
    }
}