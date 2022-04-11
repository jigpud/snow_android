package com.jigpud.snow.page.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jigpud.snow.R;

/**
 * @author : jigpud
 */
public class ScrollableSwipeToLoadLayout extends SwipeToLoadLayout {
    private ScrollableAdditionDetector scrollableAdditionDetector;

    public ScrollableSwipeToLoadLayout(Context context) {
        super(context);
    }

    public ScrollableSwipeToLoadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollableSwipeToLoadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean canChildScrollUp() {
        boolean base = super.canChildScrollUp();
        if (scrollableAdditionDetector == null) {
            return base;
        }
        boolean addition = scrollableAdditionDetector.canChildScrollUp(findViewById(R.id.swipe_target));
        return addition || base;
    }

    @Override
    protected boolean canChildScrollDown() {
        boolean base = super.canChildScrollDown();
        if (scrollableAdditionDetector == null) {
            return base;
        }
        boolean addition = scrollableAdditionDetector.canChildScrollDown(findViewById(R.id.swipe_target));
        return addition || base;
    }

    public void setScrollableAdditionDetector(ScrollableAdditionDetector scrollableAdditionDetector) {
        this.scrollableAdditionDetector = scrollableAdditionDetector;
    }

    public interface ScrollableAdditionDetector {
        boolean canChildScrollUp(View target);

        boolean canChildScrollDown(View target);
    }
}
