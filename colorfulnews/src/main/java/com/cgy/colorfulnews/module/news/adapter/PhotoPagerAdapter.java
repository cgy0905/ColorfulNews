package com.cgy.colorfulnews.module.news.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cgy.colorfulnews.module.news.fragment.PhotoDetailFragment;

import java.util.List;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/14 16:43
 */
public class PhotoPagerAdapter extends FragmentStatePagerAdapter {

    private List<PhotoDetailFragment> mFragmentList;

    public PhotoPagerAdapter(FragmentManager fm, List<PhotoDetailFragment> fragmentList) {
        super(fm);
        mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
