package com.java.wuzihan.anews.ui.NewsListActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

public class NewsListFragment extends Fragment {
    private String mCategory;
    private List<News> mNewsList;
    private NewsListFragmentViewModel mViewModel;

    // To create items inside.
    private RecyclerView mRecyclerView;
    private NewsListItemsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        Bundle bundle = this.getArguments();
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
        mViewModel.getNews().observe(this, newsObserver);
        mRecyclerView = view.findViewById(R.id.recyclerview_news_list);
        mAdapter = new NewsListItemsAdapter(view.getContext(), mNewsList, mViewModel);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

class NewsListItemsAdapter extends RecyclerView.Adapter<NewsListItemsAdapter.NewsListItemHolder> {

    private final List<News> mNewsList;
    private LayoutInflater mInflater;
    private NewsListFragmentViewModel mViewModel;

    public NewsListItemsAdapter(Context context, List<News> newsList, NewsListFragmentViewModel viewModel) {
        mInflater = LayoutInflater.from(context);
        mNewsList = newsList;
        mViewModel = viewModel;
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
                mViewModel.setNewsViewed((String) newsHeading.getText(), true);
                Intent intent = new Intent();
                intent.setClass(v.getContext(), NewsDetailsActivity.class);
                intent.putExtra("newsUrl", newsUrl.getText());
                intent.putExtra("favorite", (Boolean) newsHeading.getTag());
                intent.putExtra("title", newsHeading.getText());
                v.getContext().startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(NewsListItemsAdapter.NewsListItemHolder holder, int position) {
        holder.bind(mNewsList.get(position), mViewModel);
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
        News news;

        public NewsListItemHolder(View view, NewsListItemsAdapter adapter) {
            // TODO: change styles of item holder.
            super(view);
            mAdapter = adapter;
            newsHeading = view.findViewById(R.id.item_news_list_heading);
            newsTime = view.findViewById(R.id.item_news_list_time);
            newsLink = view.findViewById(R.id.item_news_list_link);
        }

        public void bind(final News item, NewsListFragmentViewModel viewModel) {
            news = item;
            mViewModel = viewModel;
            newsHeading.setText(item.getHeading());
            if (item.isViewed()) {
                newsHeading.setTextColor(this.itemView.getContext().getResources().getColor(R.color.unReadNews));
            } else {
                newsHeading.setTextColor(this.itemView.getContext().getResources().getColor(R.color.colorPrimary));
            }
            newsLink.setText(item.getUrl());
            newsTime.setText(item.getPubDate());
            newsHeading.setTag(item.isFavorite());
        }
    }
}
