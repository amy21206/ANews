package com.java.wuzihan.anews.ui.NewsListActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.java.wuzihan.anews.R;
import com.java.wuzihan.anews.ViewModel.NewsListFragmentViewModel;
import com.java.wuzihan.anews.ViewModel.NewsListFragmentViewModelFactory;
import com.java.wuzihan.anews.database.entity.News;
import com.java.wuzihan.anews.ViewModel.NewsListViewModel;
import com.java.wuzihan.anews.ui.NewsDetailsActivity.NewsDetailsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private String mCategory;
    private List<News> mNewsList;
    private NewsListFragmentViewModel mViewModel;

    // To create items inside.
    private RecyclerView mRecyclerView;
    private NewsListItemsAdapter mAdapter;

    protected Handler handler;


    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        Bundle bundle = this.getArguments();
        mRecyclerView = view.findViewById(R.id.recyclerview_settings_news_list);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        mCategory = bundle.getString("category");
        mViewModel =
                ViewModelProviders
                        .of(this,
                                new NewsListFragmentViewModelFactory(this.getActivity().getApplication(), mCategory))
                        .get(NewsListFragmentViewModel.class);
        mNewsList = new ArrayList<>();
        final Observer<List<News>> newsObserver = new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> newsList) {
                Log.d("onchanged", mCategory);
                Log.d("onchanged", String.valueOf(newsList.size()));
                mNewsList.clear();
                mNewsList.addAll(newsList);
                mRecyclerView = view.findViewById(R.id.recyclerview_news_list);
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        };
        handler = new Handler();
        mViewModel.getNews().observe(this, newsObserver);
        mRecyclerView = view.findViewById(R.id.recyclerview_news_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAdapter = new NewsListItemsAdapter(view.getContext(), mNewsList, mRecyclerView, mViewModel);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            int itemcount = 0;

            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                mNewsList.add(null);
                mAdapter.notifyItemInserted(mNewsList.size() - 1);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        mNewsList.remove(mNewsList.size() - 1);
                        mAdapter.notifyItemRemoved(mNewsList.size());
                        //add items one by one
                        int start = mNewsList.size();
                        int end = start + 5;
                        List<News> appendNews = mViewModel.getNewsMore(start, end);
                        for (News item : appendNews) {
                            mNewsList.add(item);
                        }
                        mAdapter.setLoaded();
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                mViewModel.refresh();
            }
        }, 2000);
    }
}

