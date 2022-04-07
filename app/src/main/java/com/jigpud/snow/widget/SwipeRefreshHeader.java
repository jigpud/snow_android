package com.jigpud.snow.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.jigpud.snow.databinding.SwipeLoadingBinding;

/**
 * @author : jigpud
 */
public class SwipeRefreshHeader extends FrameLayout implements SwipeRefreshTrigger, SwipeTrigger {
    private SwipeLoadingBinding binding;

    public SwipeRefreshHeader(@NonNull Context context) {
        super(context);
        addChildren(context);
    }

    public SwipeRefreshHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addChildren(context);
    }

    public SwipeRefreshHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addChildren(context);
    }

    public SwipeRefreshHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        addChildren(context);
    }

    @Override
    public void onRefresh() {
        binding.hint.setText("");
        startLoadingAnimation();
    }

    @Override
    public void onPrepare() {
        binding.hint.setText("");
        stopLoadingAnimation();
    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled >= getHeight()) {
                binding.hint.setText("松手刷新");
            } else {
                binding.hint.setText("下拉以刷新");
            }
        }
    }

    @Override
    public void onRelease() {
    }

    @Override
    public void onComplete() {
        binding.hint.setText("刷新完成");
        stopLoadingAnimation();
    }

    @Override
    public void onReset() {
        binding.hint.setText("");
        stopLoadingAnimation();
    }

    private void addChildren(Context context) {
        binding = SwipeLoadingBinding.inflate(LayoutInflater.from(context));
        addView(binding.getRoot());
    }

    private void startLoadingAnimation() {
        if (!binding.loadingAnimation.isAnimating()) {
            binding.loadingAnimation.playAnimation();
            binding.loadingAnimation.setAlpha(1f);
        }
    }

    private void stopLoadingAnimation() {
        binding.loadingAnimation.cancelAnimation();
        binding.loadingAnimation.setAlpha(0f);
    }
}
