package com.java.wuzihan.anews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewsDetailsActivity extends AppCompatActivity {

    private WebView mWebView;
    private String mUrl;

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
        Intent startingIntent = getIntent();
        setContentView(R.layout.activity_news_details);
        mWebView = findViewById(R.id.webview_news_details);
        mWebView.setWebViewClient(new NewsWebViewClient());
        WebSettings settings = mWebView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        mUrl = startingIntent.getStringExtra("newsUrl");
        Log.d("NewsDetailsActivity", mUrl);
        mWebView.loadUrl(mUrl);
    }
}
