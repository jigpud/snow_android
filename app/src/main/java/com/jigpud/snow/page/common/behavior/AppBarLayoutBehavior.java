package com.jigpud.snow.page.common.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.jigpud.snow.util.logger.Logger;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author : jigpud
 * 拷贝自：https://github.com/yuruiyin/AppbarLayoutBehavior，修改支持material design
 */
public class AppBarLayoutBehavior extends AppBarLayout.Behavior {
    private static final String TAG = "AppbarLayoutBehavior";
    private static final int TYPE_FLING = 1;

    private final Field flingRunnableField;
    private final Field scrollerField;

    private boolean isFlinging;
    private boolean shouldBlockNestedScroll;

    public AppBarLayoutBehavior() {
        flingRunnableField = getFlingRunnableField();
        scrollerField = getScrollerField();
    }

    public AppBarLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        flingRunnableField = getFlingRunnableField();
        scrollerField = getScrollerField();
    }

    @Override
    public boolean onInterceptTouchEvent(
            @NonNull @NotNull CoordinatorLayout parent,
            @NonNull @NotNull AppBarLayout child,
            @NonNull @NotNull MotionEvent ev
    ) {
        shouldBlockNestedScroll = isFlinging;
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            stopAppbarLayoutFling(child);
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onStartNestedScroll(
            @NonNull @NotNull CoordinatorLayout parent,
            @NonNull @NotNull AppBarLayout child,
            @NonNull @NotNull View directTargetChild,
            View target,
            int nestedScrollAxes,
            int type
    ) {
        stopAppbarLayoutFling(child);
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type);
    }

    @Override
    public void onNestedPreScroll(
            CoordinatorLayout coordinatorLayout,
            @NonNull @NotNull AppBarLayout child,
            View target,
            int dx,
            int dy,
            int[] consumed,
            int type
    ) {
        isFlinging = type == TYPE_FLING;
        if (!shouldBlockNestedScroll) {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        }
    }

    @Override
    public void onNestedScroll(
            CoordinatorLayout coordinatorLayout,
            @NonNull @NotNull AppBarLayout child,
            View target,
            int dxConsumed,
            int dyConsumed,
            int dxUnconsumed,
            int dyUnconsumed,
            int type,
            int[] consumed
    ) {
        if (!shouldBlockNestedScroll) {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
        }
    }

    @Override
    public void onStopNestedScroll(
            CoordinatorLayout coordinatorLayout,
            @NonNull @NotNull AppBarLayout abl,
            View target,
            int type
    ) {
        super.onStopNestedScroll(coordinatorLayout, abl, target, type);
        isFlinging = false;
        shouldBlockNestedScroll = false;
    }

    private void stopAppbarLayoutFling(AppBarLayout appBarLayout) {
        try {
            if (flingRunnableField != null) {
                flingRunnableField.setAccessible(true);
            }
            Runnable flingRunnable = null;
            if (flingRunnableField != null) {
                flingRunnable = (Runnable) flingRunnableField.get(this);
            }
            if (flingRunnable != null) {
                appBarLayout.removeCallbacks(flingRunnable);
                flingRunnableField.set(this, null);
            }

            if (scrollerField != null) {
                scrollerField.setAccessible(true);
            }
            OverScroller overScroller = null;
            if (scrollerField != null) {
                overScroller = (OverScroller) scrollerField.get(this);
            }
            if (overScroller != null && !overScroller.isFinished()) {
                overScroller.abortAnimation();
            }
        } catch (Exception e) {
            Logger.d(TAG, Log.getStackTraceString(e));
        }
    }

    private Field getFlingRunnableField() {
        Class<?> superclass = Objects.requireNonNull(this.getClass().getSuperclass()).getSuperclass();
        try {
            Class<?> headerBehaviorType = null;
            if (superclass != null) {
                headerBehaviorType = superclass.getSuperclass();
            }
            if (headerBehaviorType != null) {
                return headerBehaviorType.getDeclaredField("flingRunnable");
            }
        } catch (Exception e) {
            Logger.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }

    private Field getScrollerField() {
        Class<?> superclass = Objects.requireNonNull(this.getClass().getSuperclass()).getSuperclass();
        try {
            Class<?> headerBehaviorType = null;
            if (superclass != null) {
                headerBehaviorType = superclass.getSuperclass();
            }
            if (headerBehaviorType != null) {
                return headerBehaviorType.getDeclaredField("scroller");
            }
        } catch (Exception e) {
            Logger.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }
}
