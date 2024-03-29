package com.cgy.colorfulnews.module.news.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.cgy.colorfulnews.R;
import com.cgy.colorfulnews.common.Constants;
import com.cgy.colorfulnews.module.base.BaseActivity;

import butterknife.BindView;

public class NewsBrowserActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.web_view)
    WebView mWebView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_browser;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initView() {
        mToolbar.setTitle(getIntent().getStringExtra(Constants.NEWS_TITLE));
        initWebView();
    }

    private void initWebView() {
        setWebViewSettings();
        setWebView();
    }

    private void setWebViewSettings() {
        WebSettings webSettings = mWebView.getSettings();
        //打开页面时自适应屏幕
        webSettings.setUseWideViewPort(true);//将图片调整到适合WebView的大小
        webSettings.setLoadWithOverviewMode(true);//缩放只屏幕的大小
        //使页面支持缩放
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setSupportZoom(true);//支持缩放
//        webSettings.setBuiltInZoomControls(true); // 放大和缩小的按钮，容易引发异常 http://blog.csdn.net/dreamer0924/article/details/34082687

        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    private void setWebView() {
        mWebView.loadUrl(getIntent().getStringExtra(Constants.NEWS_LINK));
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.removeAllViews();
        mWebView.destroy();
    }
}
