package com.cgy.colorfulnews.di.module;

import android.app.Activity;
import android.content.Context;

import com.cgy.colorfulnews.di.scope.ContextLife;
import com.cgy.colorfulnews.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/10 15:23
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @PerActivity
    @ContextLife("Activity")
    public Context provideActivityContext() {
        return mActivity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity() {
        return mActivity;
    }
}
