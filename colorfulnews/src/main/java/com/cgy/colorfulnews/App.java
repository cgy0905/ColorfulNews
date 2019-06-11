package com.cgy.colorfulnews;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;

import com.cgy.colorfulnews.common.Constants;
import com.cgy.colorfulnews.db.DaoMaster;
import com.cgy.colorfulnews.db.DaoSession;
import com.cgy.colorfulnews.db.NewsChannelDao;
import com.cgy.colorfulnews.di.component.ApplicationComponent;
import com.cgy.colorfulnews.di.component.DaggerApplicationComponent;
import com.cgy.colorfulnews.di.module.ApplicationModule;
import com.cgy.colorfulnews.utils.MyUtils;
import com.socks.library.KLog;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/10 15:31
 */
public class App extends Application {

    private ApplicationComponent mApplicationComponent;
    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        App application = (App) context.getApplicationContext();
        return application.refWatcher;
    }

    private static Context sAppContext;
    private static DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;
        initLeakCanary();
        initActivityLifecycleLogs();
        initStrictMode();
        initDayNightMode();
        KLog.init(BuildConfig.LOG_DEBUG);
        //官方推荐将获取 DaoMaster 对象的方法放到Application层,这样将避免多次创建生成Session对象
        setupDatabase();
        initApplicationComponent();
    }

    private void initLeakCanary() {
        if (BuildConfig.DEBUG) {
            refWatcher = LeakCanary.install(this);
        } else {
            refWatcher = installLeakCanary();
        }
    }

    /**
     * release版本使用此方法
     */
    protected RefWatcher installLeakCanary() {
        return RefWatcher.DISABLED;
    }

    private void initActivityLifecycleLogs() {
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                KLog.v("=========", activity + "  onActivityCreated");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                KLog.v("=========", activity + "  onActivityStarted");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                KLog.v("=========", activity + "  onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                KLog.v("=========", activity + "  onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                KLog.v("=========", activity + "  onActivityStopped");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                KLog.v("=========", activity + "  onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                KLog.v("=========", activity + "  onActivityDestroyed");
            }
        });
    }

    private void initStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
//            .penaltyDialog() //弹出违规提示对话框
                    .penaltyLog()//在LogCat中打印违规异常信息
                    .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }
    }

    private void initDayNightMode () {
        if (MyUtils.isNightMode()) {

        }
    }

    private void setupDatabase () {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, Constants.DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        //注意:该数据库连接属于 DaoMaster 所以多个Session指的是相同的数据库连接
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
        //在 QueryBuilder 勒种内置两个 Flag 用于方便输出执行 SQL 语句与传递参数的值
        QueryBuilder.LOG_SQL  = BuildConfig.DEBUG;
        QueryBuilder.LOG_VALUES = BuildConfig.DEBUG;
    }

    public static Context getAppContext () {
        return sAppContext;
    }


    private void initApplicationComponent () {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent () {
        return mApplicationComponent;
    }

    public static NewsChannelDao getNewsChannelDao () {
        return mDaoSession.getNewsChannelDao();
    }

    public static boolean isHavePhoto () {
        return MyUtils.getSharedPreferences().getBoolean(Constants.SHOW_NEWS_PHOTO, true);
    }
}
