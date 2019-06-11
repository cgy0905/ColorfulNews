package com.cgy.colorfulnews.di.component;

import android.content.Context;

import com.cgy.colorfulnews.di.module.ApplicationModule;
import com.cgy.colorfulnews.di.scope.ContextLife;
import com.cgy.colorfulnews.di.scope.PerApp;

import dagger.Component;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/10 16:06
 */
@PerApp
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    @ContextLife("Application")
    Context getApplication();
}
