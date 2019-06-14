package com.cgy.colorfulnews.module.news.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cgy.colorfulnews.R;
import com.cgy.colorfulnews.common.Constants;
import com.cgy.colorfulnews.entity.NewsPhotoDetail;
import com.cgy.colorfulnews.event.PhotoDetailOnClickEvent;
import com.cgy.colorfulnews.module.base.BaseActivity;
import com.cgy.colorfulnews.module.news.adapter.PhotoPagerAdapter;
import com.cgy.colorfulnews.module.news.fragment.PhotoDetailFragment;
import com.cgy.colorfulnews.utils.RxBus;
import com.cgy.colorfulnews.widget.PhotoViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NewsPhotoDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.view_pager)
    PhotoViewPager mViewPager;
    @BindView(R.id.tv_photo_title)
    TextView mTvPhotoTitle;

    private List<PhotoDetailFragment> mPhotoDetailFragmentList = new ArrayList<>();
    private NewsPhotoDetail mNewsPhotoDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscription = RxBus.getInstance().toObservable(PhotoDetailOnClickEvent.class)
                .subscribe(photoDetailOnClickEvent -> {
                    if (mTvPhotoTitle.getVisibility() == View.VISIBLE) {
                        startAnimation(View.GONE, 0.9f, 0.5f);
                    } else {
                        mTvPhotoTitle.setVisibility(View.VISIBLE);
                        startAnimation(View.VISIBLE, 0.5f, 0.9f);
                    }
                });
    }

    private void startAnimation(int endState, float startValue, float endValue) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mTvPhotoTitle, "alpha", startValue, endState)
                .setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mTvPhotoTitle.setVisibility(endState);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_photo_detail;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initView() {
        mNewsPhotoDetail = getIntent().getParcelableExtra(Constants.PHOTO_DETAIL);
        createFragment(mNewsPhotoDetail);
        initViewPager();
        setPhotoDetailTitle(0);
    }

    private void createFragment(NewsPhotoDetail newsPhotoDetail) {
        mPhotoDetailFragmentList.clear();
        for (NewsPhotoDetail.Picture picture : newsPhotoDetail.getPictures()) {
            PhotoDetailFragment fragment = new PhotoDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PHOTO_DETAIL_IMGSRC, picture.getImgSrc());
            fragment.setArguments(bundle);
            mPhotoDetailFragmentList.add(fragment);
        }
    }

    private void initViewPager() {
        PhotoPagerAdapter photoPagerAdapter = new PhotoPagerAdapter(getSupportFragmentManager(), mPhotoDetailFragmentList);
        mViewPager.setAdapter(photoPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPhotoDetailTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setPhotoDetailTitle(int position) {
        String title = getTitle(position);
        mTvPhotoTitle.setText(getString(R.string.photo_detail_title, position + 1, mPhotoDetailFragmentList.size(), title));
    }

    private String getTitle(int position) {
        String title = mNewsPhotoDetail.getPictures().get(position).getTitle();
        if (title == null) {
            title = mNewsPhotoDetail.getTitle();
        }
        return title;
    }


}
