package com.cgy.colorfulnews.module.news.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cgy.colorfulnews.App;
import com.cgy.colorfulnews.R;
import com.cgy.colorfulnews.common.Constants;
import com.cgy.colorfulnews.entity.NewsDetail;
import com.cgy.colorfulnews.module.base.BaseActivity;
import com.cgy.colorfulnews.module.news.presenter.impl.NewsDetailPresenterImpl;
import com.cgy.colorfulnews.module.news.view.NewsDetailView;
import com.cgy.colorfulnews.utils.MyUtils;
import com.cgy.colorfulnews.utils.NetUtil;
import com.cgy.colorfulnews.utils.TransformUtils;
import com.cgy.colorfulnews.widget.URLImageGetter;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.socks.library.KLog;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;

public class NewsDetailActivity extends BaseActivity implements NewsDetailView {

    @BindView(R.id.iv_photo_detail)
    ImageView mIvPhotoDetail;
    @BindView(R.id.mask_view)
    View mMaskView;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.tv_news_from)
    TextView mTvNewsFrom;
    @BindView(R.id.tv_news_detail)
    TextView mTvNewsDetail;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @Inject
    NewsDetailPresenterImpl mNewsDetailPresenter;

    private URLImageGetter mURLImageGetter;
    private String mNewsTitle;
    private String mShareLink;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initView() {
        String postId = getIntent().getStringExtra(Constants.NEWS_POST_ID);
        mNewsDetailPresenter.setPostId(postId);
        mPresenter = mNewsDetailPresenter;
        mPresenter.attachView(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setNewsDetail(NewsDetail newsDetail) {
        mShareLink = newsDetail.getShareLink();
        mNewsTitle = newsDetail.getTitle();
        String newsSource = newsDetail.getSource();
        String newsTime = MyUtils.formatDate(newsDetail.getPtime());
        String newsBody = newsDetail.getBody();
        String newsImaSrc = getImgSrcs(newsDetail);

        setToolbarLayout(mNewsTitle);
        mTvNewsFrom.setText(getString(R.string.news_from, newsSource, newsTime));
        setIvPhotoNewsDetail(newsImaSrc);
        setTvNewsDetailBody(newsDetail, newsBody);
    }

    private void setToolbarLayout(String newsTitle) {
        mToolbarLayout.setTitle(newsTitle);
        mToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white));
        mToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.primary_text_white));
    }

    private void setIvPhotoNewsDetail(String imgSrc) {
        Glide.with(this)
                .load(imgSrc)
                .asBitmap()
                .placeholder(R.drawable.ic_loading)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .error(R.drawable.ic_load_fail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mIvPhotoDetail);
    }

    private void setTvNewsDetailBody(final NewsDetail newsDetail, final String newsBody) {
        mSubscription = Observable.timer(500, TimeUnit.MILLISECONDS)
                .compose(TransformUtils.defaultSchedulers())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        mProgressBar.setVisibility(View.GONE);
                        mFab.show();
                        YoYo.with(Techniques.RollIn).playOn(mFab);

                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        setBody(newsDetail, newsBody);
                    }
                });
    }

    private void setBody(NewsDetail newsDetail, String newsBody) {
        int imgTotal = newsDetail.getImg().size();
        if (isShowBody(newsBody, imgTotal)) {
            mURLImageGetter = new URLImageGetter(mTvNewsDetail, newsBody, imgTotal);
            mTvNewsDetail.setText(Html.fromHtml(newsBody, mURLImageGetter, null));
        } else {
            mTvNewsDetail.setText(Html.fromHtml(newsBody));
        }
    }

    private boolean isShowBody(String newsBody, int imgTotal) {
        return App.isHavePhoto() && imgTotal >= 2 && newsBody != null;
    }

    private String getImgSrcs(NewsDetail newsDetail) {
        List<NewsDetail.ImgBean> imgBeans = newsDetail.getImg();
        String imgSrc;
        if (imgBeans != null && imgBeans.size() > 0) {
            imgSrc = imgBeans.get(0).getSrc();
        } else {
            imgSrc = getIntent().getStringExtra(Constants.NEWS_IMG_RES);
        }
        return imgSrc;
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String message) {
        mProgressBar.setVisibility(View.GONE);
        if (NetUtil.isNetworkAvailable()) {
            Snackbar.make(mAppBar, message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_web_view:
                openByWebView();
                break;
            case R.id.action_browser:
                openByBrowser();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openByWebView() {
        Intent intent = new Intent(this, NewsBrowserActivity.class);
        intent.putExtra(Constants.NEWS_LINK, mShareLink);
        intent.putExtra(Constants.NEWS_TITLE, mNewsTitle);
        startActivity(intent);
    }

    private void openByBrowser() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        if (canBrowse(intent)) {
            Uri uri = Uri.parse(mShareLink);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    private boolean canBrowse(Intent intent) {
        return intent.resolveActivity(getPackageManager()) != null && mShareLink != null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelUrlImageGetterSubscription();
    }

    private void cancelUrlImageGetterSubscription() {
        try {
            if (mURLImageGetter != null && mURLImageGetter.mSubscription != null
                    && !mURLImageGetter.mSubscription.isUnsubscribed()) {
                mURLImageGetter.mSubscription.unsubscribe();
                KLog.d("URLImageGetter unsubscribe");
            }
        } catch (Exception e) {
            KLog.e("取消UrlImageGetter Subscription 出现异常： " + e.toString());
        }
    }

    @OnClick(R.id.fab)
    public void onClick() {
        share();
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share));
        intent.putExtra(Intent.EXTRA_TEXT, getShareContents());
        startActivity(Intent.createChooser(intent, getTitle()));
    }

    @NonNull
    private String getShareContents() {
       if (mShareLink == null) {
           mShareLink = "";
       }
       return getString(R.string.share_contents, mNewsTitle, mShareLink);
    }

}
