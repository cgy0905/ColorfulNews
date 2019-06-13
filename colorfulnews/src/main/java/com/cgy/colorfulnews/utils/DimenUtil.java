package com.cgy.colorfulnews.utils;

import com.cgy.colorfulnews.App;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/13 10:10
 */
public class DimenUtil {

    public static float dp2px(float dpVal) {
        final float scale = App.getAppContext().getResources().getDisplayMetrics().density;
        return dpVal * scale + 0.5f;
    }

    public static float sp2px(float spVal) {
        final float scale = App.getAppContext().getResources().getDisplayMetrics().scaledDensity;
        return spVal * scale;
    }

    public static int getScreenSize() {
        return App.getAppContext().getResources().getDisplayMetrics().widthPixels;
    }
}
