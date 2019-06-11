package com.cgy.colorfulnews.di.module;

import android.app.Service;
import android.content.Context;

import com.cgy.colorfulnews.di.scope.ContextLife;
import com.cgy.colorfulnews.di.scope.PerService;

import dagger.Module;
import dagger.Provides;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/10 15:28
 */
@Module
public class ServiceModule {

    private Service mService;

    public ServiceModule(Service service) {
        mService = service;
    }

    @Provides
    @PerService
    @ContextLife("Service")
    public Context provideServiceContext() {
        return mService;
    }
}
