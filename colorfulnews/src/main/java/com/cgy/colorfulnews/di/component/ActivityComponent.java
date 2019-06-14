package com.cgy.colorfulnews.di.component;

import android.app.Activity;
import android.content.Context;

import com.cgy.colorfulnews.di.module.ActivityModule;
import com.cgy.colorfulnews.di.scope.ContextLife;
import com.cgy.colorfulnews.di.scope.PerActivity;
import com.cgy.colorfulnews.module.news.activity.NewsActivity;
import com.cgy.colorfulnews.module.news.activity.NewsChannelActivity;
import com.cgy.colorfulnews.module.news.activity.NewsDetailActivity;

import dagger.Component;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/10 16:05
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(NewsActivity newsActivity);

    void inject(NewsChannelActivity newsChannelActivity);

    void inject(NewsDetailActivity newsDetailActivity);


}
