package com.cgy.colorfulnews.di.module;

import android.content.Context;

import com.cgy.colorfulnews.App;
import com.cgy.colorfulnews.di.scope.ContextLife;
import com.cgy.colorfulnews.di.scope.PerApp;

import dagger.Module;
import dagger.Provides;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/10 15:29
 */
@Module
public class ApplicationModule {

    private App mApplication;

    public ApplicationModule(App application) {
        mApplication = application;
    }

    @Provides
    @PerApp
    @ContextLife("Application")
    public Context provideApplicationContext() {
        return mApplication.getApplicationContext();
    }
}
