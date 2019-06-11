package com.cgy.colorfulnews.di.component;

import android.app.Activity;
import android.content.Context;

import com.cgy.colorfulnews.di.module.ActivityModule;
import com.cgy.colorfulnews.di.scope.ContextLife;
import com.cgy.colorfulnews.di.scope.PerActivity;

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


}
