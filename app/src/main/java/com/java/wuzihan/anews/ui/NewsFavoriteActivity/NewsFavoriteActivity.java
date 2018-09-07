package com.java.wuzihan.anews.ui.NewsFavoriteActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.java.wuzihan.anews.R;
import com.java.wuzihan.anews.ViewModel.NewsFavoriteViewModel;
import com.java.wuzihan.anews.database.entity.News;
import com.java.wuzihan.anews.ui.NewsDetailsActivity.NewsDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class NewsFavoriteActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    NewsFavoriteViewModel mViewModel;
    NewsListItemsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_favorite);
        mRecyclerView = findViewById(R.id.recyclerview_news_favorite);
        mViewModel = ViewModelProviders.of(this).get(NewsFavoriteViewModel.class);

        final List<News> newsList = new ArrayList<>();
        final Observer<List<News>> categoryObserver = new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> favoriteNews) {
                newsList.clear();
                newsList.addAll(favoriteNews);
                mRecyclerView = findViewById(R.id.recyclerview_news_favorite);
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        };
        mViewModel.getFavoriteNews().observe(this, categoryObserver);

        mAdapter = new NewsListItemsAdapter(this.getApplicationContext(), newsList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
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
                TextView newsUrl = v.findViewById(R.id.item_news_list_link);
                TextView newsHeading = v.findViewById(R.id.item_news_list_heading);
                Intent intent = new Intent();
                intent.setClass(v.getContext(), NewsDetailsActivity.class);
                intent.putExtra("newsUrl", newsUrl.getText());
                intent.putExtra("favorited", false);
                intent.putExtra("title", newsHeading.getText());
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
        TextView newsLink;

        public NewsListItemHolder(View view, NewsListItemsAdapter adapter) {
            // TODO: change styles of item holder.
            super(view);
            mAdapter = adapter;
            newsHeading = view.findViewById(R.id.item_news_list_heading);
            newsTime = view.findViewById(R.id.item_news_list_time);
            newsLink = view.findViewById(R.id.item_news_list_link);
        }

        public void bind(final News item) {
            newsHeading.setText(item.getHeading());
            newsLink.setText(item.getUrl());
            newsTime.setText(item.getPubDate());
        }
    }
}
