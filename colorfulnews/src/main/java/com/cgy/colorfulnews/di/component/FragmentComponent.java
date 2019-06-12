package com.cgy.colorfulnews.di.component;

import android.app.Activity;
import android.content.Context;

import com.cgy.colorfulnews.di.module.FragmentModule;
import com.cgy.colorfulnews.di.scope.ContextLife;
import com.cgy.colorfulnews.di.scope.PerFragment;
import com.cgy.colorfulnews.module.news.fragment.NewsListFragment;

import dagger.Component;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/10 16:08
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(NewsListFragment newsListFragment);

}
