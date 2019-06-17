package com.cgy.colorfulnews.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/17 9:58
 */
public class PullBackLayout extends FrameLayout {

    private final ViewDragHelper mDragHelper;

    private final int minimumFlingVelocity;

    @Nullable
    private Callback callback;

    public PullBackLayout( Context context) {
        this(context, null);
    }

    public PullBackLayout( Context context,  AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullBackLayout( Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this, 1f / 8f, new ViewDragCallback());//   1f/8f 是敏感度参数 参数越大越敏感
        minimumFlingVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
    }

    public void setCallback(@Nullable Callback callback) {
        this.callback = callback;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return mDragHelper.shouldInterceptTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        try {
            mDragHelper.processTouchEvent(event);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public interface Callback{

        void onPullStart();

        void onPull(float progress);

        void onPullCancel();

        void onPullComplete();
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            return 0;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return Math.max(0, top);
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return 0;
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return getHeight();
        }

        @Override
        public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
            if (callback != null) {
                callback.onPullStart();
            }
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            if (callback != null) {
                callback.onPull((float) top / (float) getHeight());
            }
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            int slap = yvel > minimumFlingVelocity ? getHeight() / 6 : getHeight() / 3;
            if (releasedChild.getTop() > slap) {
                if (callback != null) {
                    callback.onPullComplete();
                }
            } else {
                if (callback != null) {
                    callback.onPullCancel();
                }

                mDragHelper.settleCapturedViewAt(0, 0);
                invalidate();
            }
        }
    }
}
