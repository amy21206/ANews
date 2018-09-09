package com.java.wuzihan.anews.ui.NewsDetailsActivity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.java.wuzihan.anews.R;
import com.java.wuzihan.anews.ViewModel.NewsDetailsViewModel;

public class NewsDetailsActivity extends AppCompatActivity {

    private boolean favorite;
    private NewsDetailsViewModel mViewModel;
    private String mTitle;
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
        mViewModel = ViewModelProviders.of(this).get(NewsDetailsViewModel.class);
        setTheme(mViewModel.getTheme().getNoAppBarId());
        setContentView(R.layout.activity_news_details);

        Toolbar toolbar = findViewById(R.id.toolbar_news_details);
        setSupportActionBar(toolbar);

        WebView mWebView;
        Intent startingIntent = getIntent();
        mWebView = findViewById(R.id.webview_news_details);
        mWebView.setWebViewClient(new NewsWebViewClient());
        WebSettings settings = mWebView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        mUrl = startingIntent.getStringExtra("newsUrl");
        favorite = startingIntent.getBooleanExtra("favorite", false);
        mTitle = startingIntent.getStringExtra("title");
        mWebView.loadUrl(mUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news_details, menu);
        if (favorite) {
            MenuItem menuFavorite = menu.findItem(R.id.settings_favorite_news);
            menuFavorite.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_news_chose));
        }
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
            favorite = !favorite;
            if (favorite) {
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_news_chose));
            } else {
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_news));
            }
            mViewModel.setNewsFavorite(mTitle, favorite);
            return true;
        } else if (id == R.id.settings_share) {
            Intent shareIntent;
            /*
            Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Share.png";
            OutputStream out = null;
            File file=new File(path);
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            path=file.getPath();
            Uri bmpUri = Uri.parse("file://"+path);*/
            shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "ANews-分享新闻：\n" + mTitle + "\n" + mUrl);
            shareIntent.setType("image/png");
            startActivity(Intent.createChooser(shareIntent, "Share with"));
        }

        return super.onOptionsItemSelected(item);
    }
}
