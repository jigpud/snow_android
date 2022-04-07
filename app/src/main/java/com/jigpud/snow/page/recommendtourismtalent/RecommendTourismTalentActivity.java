package com.jigpud.snow.page.recommendtourismtalent;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.annotation.Nullable;
import bolts.Task;
import com.jigpud.snow.R;
import com.jigpud.snow.page.base.BaseActivity;
import com.jigpud.snow.databinding.RecommendTourismTalentBinding;

/**
 * @author jigpud
 */
public class RecommendTourismTalentActivity extends BaseActivity<RecommendTourismTalentBinding> {
    @Override
    protected void preSetContent(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.preSetContent(savedInstanceState);
        setUseLightStatusBar(true);
    }

    @Override
    protected void initView() {
        super.initView();

        binding.refresh.setOnClickListener(this::onRefreshClick);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRefreshAnimation();
    }

    private void onRefreshClick(View target) {
        startRefreshAnimation();
        Task.delay(3300).continueWith(task -> {
            Task.call(() -> {
                stopRefreshAnimation();
                return null;
            }, Task.UI_THREAD_EXECUTOR);
            return null;
        });
    }

    private void startRefreshAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_refresh_btn);
        binding.refresh.startAnimation(animation);
    }

    private void stopRefreshAnimation() {
        binding.refresh.clearAnimation();
    }

    @Override
    protected RecommendTourismTalentBinding createViewBinding() {
        return RecommendTourismTalentBinding.inflate(getLayoutInflater());
    }
}
