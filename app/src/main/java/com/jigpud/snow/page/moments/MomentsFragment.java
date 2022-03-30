package com.jigpud.snow.page.moments;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import bolts.Task;
import com.jigpud.snow.base.BaseFragment;
import com.jigpud.snow.databinding.MomentsBinding;
import com.jigpud.snow.page.recommendsocialgroup.RecommendSocialGroupActivity;
import com.jigpud.snow.page.recommendtourismtalent.RecommendTourismTalentActivity;

/**
 * @author jigpud
 */
public class MomentsFragment extends BaseFragment<MomentsBinding> {
    @Override
    protected void initView() {
        super.initView();

        binding.moreTourismTalent.setOnClickListener(this::onMoreTourismTalentClick);

        binding.moreSocialGroup.setOnClickListener(this::onMoreSocialGroupClick);
    }

    @Override
    public void onResume() {
        super.onResume();
        useLightStatusBar();
    }

    private void onMoreTourismTalentClick(View target) {
        startActivity(new Intent(getContext(), RecommendTourismTalentActivity.class));
    }

    private void onMoreSocialGroupClick(View target) {
        startActivity(new Intent(getContext(), RecommendSocialGroupActivity.class));
    }

    private void useLightStatusBar() {
        Task.call(() -> {
            setUseLightStatusBar(true);
            return null;
        }, Task.UI_THREAD_EXECUTOR);
    }

    @Override
    protected MomentsBinding createViewBinding(LayoutInflater inflater, ViewGroup container) {
        return MomentsBinding.inflate(inflater, container, false);
    }
}
