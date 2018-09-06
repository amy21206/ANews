package com.java.wuzihan.anews.ui.NewsDetailsActivity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.java.wuzihan.anews.R;

public class NewsDetailsActivity extends AppCompatActivity {

    private WebView mWebView;
    private String mUrl;
    private boolean favorited;

    private class NewsWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    // TODO: the back arrow is not working correctly. Fix it.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        Toolbar toolbar = findViewById(R.id.toolbar_news_details);
        setSupportActionBar(toolbar);

        Intent startingIntent = getIntent();
        mWebView = findViewById(R.id.webview_news_details);
        mWebView.setWebViewClient(new NewsWebViewClient());
        WebSettings settings = mWebView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        mUrl = startingIntent.getStringExtra("newsUrl");
        favorited = startingIntent.getBooleanExtra("favorited", false);
        Log.d("NewsDetailsActivity", mUrl);
        mWebView.loadUrl(mUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings_favorite_news) {
            favorited = !favorited;
            if (favorited) {
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_news_chose));
            } else {
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_news));
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
