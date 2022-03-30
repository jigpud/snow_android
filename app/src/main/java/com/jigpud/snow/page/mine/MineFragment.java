package com.jigpud.snow.page.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import bolts.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.jigpud.snow.base.BaseFragment;
import com.jigpud.snow.databinding.MineBinding;
import com.jigpud.snow.util.format.IntegerFormatter;

/**
 * @author jigpud
 */
public class MineFragment extends BaseFragment<MineBinding> {
    private static final String TAG = "MineFragment";
    private static final Fragment[] fragments = new Fragment[] {
            new StoryListFragment(),
            new StoryListFragment()
    };

    private MineViewModel viewModel;
    private CollapsingToolbarLayoutState myProfileSate = CollapsingToolbarLayoutState.EXPANDED;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = getViewModel(MineViewModel.class, MineViewModelFactory.create());
    }

    @Override
    protected void initView() {
        super.initView();

        observeNotNull(viewModel.getMyProfile(), userEntity -> {
            binding.signature.setText(userEntity.getSignature());
            binding.nickname.setText(userEntity.getNickname());
            binding.likesCount.setText(IntegerFormatter.formatWithUnit(userEntity.getLikes()));
            binding.fansCount.setText(IntegerFormatter.formatWithUnit(userEntity.getFollowers()));
            binding.followCount.setText(IntegerFormatter.formatWithUnit(userEntity.getFollowed()));
        });

        binding.storyList.setAdapter(new StoryFragmentStateAdapter(fragments, getChildFragmentManager(), getLifecycle()));

        binding.myProfile.addOnOffsetChangedListener(this::onMyProfileOffsetChanged);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateStatusBar(myProfileSate);
    }

    private void onMyProfileOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            if (myProfileSate != CollapsingToolbarLayoutState.EXPANDED) {
                myProfileSate = CollapsingToolbarLayoutState.EXPANDED;
            }
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (myProfileSate != CollapsingToolbarLayoutState.COLLAPSED) {
                myProfileSate = CollapsingToolbarLayoutState.COLLAPSED;
            }
        } else {
            if (myProfileSate != CollapsingToolbarLayoutState.DRAGGING) {
                myProfileSate = CollapsingToolbarLayoutState.DRAGGING;
            }
        }
        onMyProfileExpandStateChanged(appBarLayout, verticalOffset, myProfileSate);
    }

    private void onMyProfileExpandStateChanged(
            AppBarLayout appBarLayout,
            int verticalOffset,
            CollapsingToolbarLayoutState state
    ) {
        float headerAlpha = Math.abs(verticalOffset) * 1.0f / appBarLayout.getTotalScrollRange();
        binding.header.setAlpha(headerAlpha);
        binding.header.setClickable(state == CollapsingToolbarLayoutState.EXPANDED);
        updateStatusBar(state);
    }

    private void updateStatusBar(CollapsingToolbarLayoutState state) {
        Task.call(() -> {
            setUseLightStatusBar(state == CollapsingToolbarLayoutState.COLLAPSED);
            return null;
        }, Task.UI_THREAD_EXECUTOR);
    }

    @Override
    protected MineBinding createViewBinding(LayoutInflater inflater, ViewGroup container) {
        return MineBinding.inflate(inflater, container, false);
    }

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        DRAGGING
    }
}
