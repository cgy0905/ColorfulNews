package com.cgy.colorfulnews.module.news.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/12 14:01
 */
public class NewsFragmentPagerAdapter extends FragmentPagerAdapter {

    private final List<String> mTitles;
    private List<Fragment> mNewsFragmentList;

    public NewsFragmentPagerAdapter(FragmentManager fm, List<String> titles, List<Fragment> newsFragmentList) {
        super(fm);
        mTitles = titles;
        mNewsFragmentList = newsFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mNewsFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mNewsFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
