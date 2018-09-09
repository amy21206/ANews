package com.java.wuzihan.anews.ui.NewsListActivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.java.wuzihan.anews.R;
import com.java.wuzihan.anews.ViewModel.NewsListFragmentViewModel;
import com.java.wuzihan.anews.database.entity.News;
import com.java.wuzihan.anews.ui.NewsDetailsActivity.NewsDetailsActivity;

import java.util.List;

public class NewsListItemsAdapter extends RecyclerView.Adapter {

    private final List<News> mNewsList;
    private LayoutInflater mInflater;
    private NewsListFragmentViewModel mViewModel;
    private RecyclerView recyclerView;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public NewsListItemsAdapter(Context context, List<News> newsList, RecyclerView recyclerView, NewsListFragmentViewModel viewModel) {
        mInflater = LayoutInflater.from(context);
        mNewsList = newsList;
        mViewModel = viewModel;
        this.recyclerView = recyclerView;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition();
                    if (!loading
                            && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mNewsList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.item_news_preview, parent, false);
        if (viewType == VIEW_ITEM) {
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
        } else {
            RecyclerView.ViewHolder vh;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progressbar, parent, false);
            vh = new ProgressViewHolder(v);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsListItemHolder) {
            NewsListItemHolder holder1 = (NewsListItemHolder) holder;
            holder1.bind(mNewsList.get(position), mViewModel);
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public class NewsListItemHolder extends RecyclerView.ViewHolder {

        TextView newsHeading;
        final NewsListItemsAdapter mAdapter;
        TextView newsTime;
        TextView newsLink;
        TextView newsContent;
        News news;

        public NewsListItemHolder(View view, NewsListItemsAdapter adapter) {
            // TODO: change styles of item holder.
            super(view);
            mAdapter = adapter;
            newsHeading = view.findViewById(R.id.item_news_list_heading);
            newsTime = view.findViewById(R.id.item_news_list_time);
            newsLink = view.findViewById(R.id.item_news_list_link);
            newsContent = view.findViewById(R.id.item_news_list_content);
        }

        public void bind(final News item, NewsListFragmentViewModel viewModel) {
            news = item;
            mViewModel = viewModel;
            newsHeading.setText(item.getHeading());
            if (item.isViewed()) {
                newsHeading.setTextColor(this.itemView.getContext().getResources().getColor(R.color.unReadNews));
            } else {
                TypedValue typedValue = new TypedValue();
                viewModel.getTheme().getAppBarId();
                this.itemView.getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
                newsHeading.setTextColor(this.itemView.getContext().getResources().getColor(typedValue.resourceId));
            }
            newsLink.setText(item.getUrl());
            newsTime.setText(item.getPubDate());
            newsContent.setText(item.getContent());
            newsHeading.setTag(item.isFavorite());
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }
}
