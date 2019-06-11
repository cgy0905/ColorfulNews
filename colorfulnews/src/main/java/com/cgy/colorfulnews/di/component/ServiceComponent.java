package com.cgy.colorfulnews.di.component;

import android.content.Context;

import com.cgy.colorfulnews.di.module.ServiceModule;
import com.cgy.colorfulnews.di.scope.ContextLife;
import com.cgy.colorfulnews.di.scope.PerService;

import dagger.Component;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/10 16:10
 */
@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {

    @ContextLife("Service")
    Context getServiceContext();

    @ContextLife("Application")
    Context getApplicationContext();
}
