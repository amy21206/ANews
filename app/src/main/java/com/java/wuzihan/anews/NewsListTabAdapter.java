package com.java.wuzihan.anews;

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
        return new NewsListFragment();
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