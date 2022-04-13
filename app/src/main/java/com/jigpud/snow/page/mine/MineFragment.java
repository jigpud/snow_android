package com.jigpud.snow.page.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import bolts.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.jigpud.snow.databinding.MineBinding;
import com.jigpud.snow.page.base.BaseFragment;
import com.jigpud.snow.page.common.adapter.UserStoryListAdapter;
import com.jigpud.snow.page.common.widget.ScrollableSwipeToLoadLayout;
import com.jigpud.snow.util.format.IntegerFormatter;
import com.jigpud.snow.util.logger.Logger;

/**
 * @author jigpud
 */
public class MineFragment extends BaseFragment<MineBinding> implements UserStoryListAdapter.StoryClickListener,
        ScrollableSwipeToLoadLayout.ScrollableAdditionDetector {
    private static final String TAG = "MineFragment";

    private MineViewModel mineViewModel;
    private CollapsingToolbarLayoutState myProfileSate = CollapsingToolbarLayoutState.EXPANDED;
    private UserStoryListAdapter storyListAdapter;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mineViewModel = getViewModel(MineViewModel.class, MineViewModelFactory.create());
        storyListAdapter = new UserStoryListAdapter(MineViewModel.MY_STORY_LIST_PAGE_SIZE, this);
    }

    @Override
    protected void initView() {
        super.initView();

        observeNotNull(mineViewModel.getMyProfile(), userEntity -> {
            binding.signature.setText(userEntity.getSignature());
            binding.nickname.setText(userEntity.getNickname());
            binding.likesCount.setText(IntegerFormatter.formatWithUnit(userEntity.getLikes()));
            binding.fansCount.setText(IntegerFormatter.formatWithUnit(userEntity.getFollowers()));
            binding.followCount.setText(IntegerFormatter.formatWithUnit(userEntity.getFollowed()));
        });

        binding.myStory.setOnLoadMoreListener(this::onLoadMore);
        binding.myStory.setOnRefreshListener(this::onRefresh);
        binding.myStory.setScrollableAdditionDetector(this);

        binding.swipeTarget.setAdapter(storyListAdapter);
        binding.swipeTarget.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.swipeTarget.setItemAnimator(null);

        binding.myProfile.addOnOffsetChangedListener(this::onMyProfileOffsetChanged);

        onRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateStatusBar(myProfileSate);
    }

    @Override
    public void onStoryClick(String storyId) {

    }

    @Override
    public void onLike(String storyId) {
        observeNotNull(mineViewModel.likeStory(storyId), likeStoryStatus -> {
            if (likeStoryStatus.first) {
                observeNotNull(mineViewModel.getStory(storyId), storyListAdapter::updateRecord);
            }
        });
    }

    @Override
    public void onUnlike(String storyId) {
        observeNotNull(mineViewModel.unlikeStory(storyId), likeStoryStatus -> {
            if (likeStoryStatus.first) {
                observeNotNull(mineViewModel.getStory(storyId), storyListAdapter::updateRecord);
            }
        });
    }

    @Override
    public boolean canChildScrollUp(View target) {
        Logger.d(TAG, "canChildScrollUp: %s", myProfileSate.compareTo(CollapsingToolbarLayoutState.EXPANDED) != 0);
        return myProfileSate.compareTo(CollapsingToolbarLayoutState.EXPANDED) != 0;
    }

    @Override
    public boolean canChildScrollDown(View target) {
        Logger.d(TAG, "canChildScrollDown: %s", myProfileSate.compareTo(CollapsingToolbarLayoutState.COLLAPSED) != 0);
        return myProfileSate.compareTo(CollapsingToolbarLayoutState.COLLAPSED) != 0;
    }

    private void onRefresh() {
        binding.myStory.setRefreshing(true);
        observeNotNull(mineViewModel.refreshMyStoryList(), myStory -> {
            binding.myStory.setRefreshing(false);
            storyListAdapter.setRecords(myStory);
            binding.myStory.setLoadMoreEnabled(myStory.size() >= MineViewModel.MY_STORY_LIST_PAGE_SIZE);
        });
    }

    private void onLoadMore() {
        binding.myStory.setLoadingMore(true);
        observeNotNull(mineViewModel.moreStoryList(), myStory -> {
            binding.myStory.setLoadingMore(false);
            storyListAdapter.addRecords(myStory);
            binding.myStory.setLoadMoreEnabled(myStory.size() >= MineViewModel.MY_STORY_LIST_PAGE_SIZE);
        });
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
