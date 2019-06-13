package com.cgy.colorfulnews.utils;

import android.os.SystemClock;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/13 16:16
 */
public class ClickUtil {

    private static long mLastClickTime = 0;
    private static final int SPACE_TIME =  500;

    public static boolean isFastDoubleClick() {
        long time = SystemClock.elapsedRealtime();
        if (time - mLastClickTime <= SPACE_TIME) {
            return true;
        } else {
            mLastClickTime = time;
            return false;
        }
    }
}
