package com.java.wuzihan.anews;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.java.wuzihan.anews.database.News;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsListFragment extends Fragment {
    private String mCategory;
    private List<News> mNewsList;
    private boolean rendered;
    private String text;
    private NewsListViewModel mViewModel;
    private List<News> tmpNewsList;

    // To create items inside.
    private RecyclerView mRecyclerView;
    private NewsListItemsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        mViewModel = ViewModelProviders.of(getActivity()).get(NewsListViewModel.class);
        Bundle bundle = this.getArguments();
        mCategory = bundle.getString("category");
        mViewModel.updateCategory(mCategory);
        Log.d("category", mCategory);
        rendered = false;
        text = "tab";
        mNewsList = new ArrayList<>();
        final Observer<HashMap<String, List<News>>> newsObserver = new Observer<HashMap<String, List<News>>>() {
            @Override
            public void onChanged(HashMap<String, List<News>> categoryToNewsList) {
                if (!rendered && categoryToNewsList.containsKey(mCategory)) {
                    rendered = true;
                    mNewsList.clear();
                    mNewsList.addAll(categoryToNewsList.get(mCategory));
                    mRecyclerView = view.findViewById(R.id.recyclerview_news_list);
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        };
        mViewModel.getCategoryToNewsList().observe(this, newsObserver);
        mRecyclerView = view.findViewById(R.id.recyclerview_news_list);
        mAdapter = new NewsListItemsAdapter(view.getContext(), mNewsList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("category", mCategory + " onDestroy");
    }
}

class NewsListItemsAdapter extends RecyclerView.Adapter<NewsListItemsAdapter.NewsListItemHolder> {

    private final List<News> mNewsList;
    private LayoutInflater mInflater;

    public NewsListItemsAdapter(Context context, List<News> newsList) {
        mInflater = LayoutInflater.from(context);
        mNewsList = newsList;
    }

    @Override
    public NewsListItemsAdapter.NewsListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.item_news_list, parent, false);
        NewsListItemHolder holder = new NewsListItemHolder(mItemView, this);
        mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NewsListItemsAdapter", "clicked");
                TextView newsUrl = v.findViewById(R.id.item_news_list_link);
                Intent intent = new Intent();
                intent.setClass(v.getContext(), NewsDetailsActivity.class);
                intent.putExtra("newsUrl", newsUrl.getText());
                v.getContext().startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(NewsListItemsAdapter.NewsListItemHolder holder, int position) {
        holder.bind(mNewsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    class NewsListItemHolder extends RecyclerView.ViewHolder {

        TextView newsHeading;
        final NewsListItemsAdapter mAdapter;
        TextView newsTime;
        TextView newsContent;
        TextView newsLink;

        public NewsListItemHolder(View view, NewsListItemsAdapter adapter) {
            // TODO: change styles of item holder.
            super(view);
            mAdapter = adapter;
            newsHeading = view.findViewById(R.id.item_news_list_heading);
            newsTime = view.findViewById(R.id.item_news_list_time);
//            newsContent = view.findViewById(R.id.item_news_list_content);
            newsLink = view.findViewById(R.id.item_news_list_link);
        }

        public void bind(final News item) {
            newsHeading.setText(item.getHeading());
            newsLink.setText(item.getUrl());
            newsTime.setText(item.getPubDate());
        }
    }
}
