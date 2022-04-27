package com.jigpud.snow.page.mine;

import android.content.Intent;
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
import com.jigpud.snow.page.common.adapter.StoryListAdapter;
import com.jigpud.snow.page.common.adapter.UserStoryListAdapter;
import com.jigpud.snow.page.common.widget.ScrollableSwipeToLoadLayout;
import com.jigpud.snow.page.storydetail.StoryDetailActivity;
import com.jigpud.snow.util.constant.KeyConstant;
import com.jigpud.snow.util.format.IntegerFormatter;
import com.jigpud.snow.util.logger.Logger;

/**
 * @author : jigpud
 */
public class MineFragment extends BaseFragment<MineBinding> implements StoryListAdapter.StoryClickListener,
        ScrollableSwipeToLoadLayout.ScrollableAdditionDetector {
    private static final String TAG = "MineFragment";

    private MineViewModel mineViewModel;
    private CollapsingToolbarLayoutState myProfileSate = CollapsingToolbarLayoutState.EXPANDED;
    private StoryListAdapter myStoryListAdapter;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mineViewModel = getViewModel(MineViewModel.class, MineViewModelFactory.create());
        myStoryListAdapter = new UserStoryListAdapter(MineViewModel.MY_STORY_LIST_PAGE_SIZE, this);
    }

    @Override
    protected void initView() {
        super.initView();

        observeNotNull(mineViewModel.getMyProfile(), userEntity -> {
            binding.signature.setText(userEntity.getSignature());
            binding.nickname.setText(userEntity.getNickname());
            binding.likesCount.setText(IntegerFormatter.formatWithUnit(userEntity.getLikes()));
            binding.fansCount.setText(IntegerFormatter.formatWithUnit(userEntity.getFollowers()));
            binding.followCount.setText(IntegerFormatter.formatWithUnit(userEntity.getFollowing()));
        });

        binding.myStoryList.setOnLoadMoreListener(this::onLoadMore);
        binding.myStoryList.setOnRefreshListener(this::onRefresh);
        binding.myStoryList.setScrollableAdditionDetector(this);

        binding.swipeTarget.setAdapter(myStoryListAdapter);
        binding.swipeTarget.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.swipeTarget.setItemAnimator(null);

        binding.myProfile.addOnOffsetChangedListener(this::onMyProfileOffsetChanged);

        autoRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateStatusBar(myProfileSate);
    }

    @Override
    public void onStoryClick(String storyId) {
        Intent intent = new Intent(requireContext(), StoryDetailActivity.class);
        intent.putExtra(KeyConstant.KEY_STORY_ID, storyId);
        startActivity(intent);
    }

    @Override
    public void onLikeStory(String storyId) {
        observeNotNull(mineViewModel.likeStory(storyId), likeStoryStatus -> {
            if (likeStoryStatus.first) {
                observeNotNull(mineViewModel.getStory(storyId), myStoryListAdapter::updateRecord);
            }
        });
    }

    @Override
    public void onUnlikeStory(String storyId) {
        observeNotNull(mineViewModel.unlikeStory(storyId), likeStoryStatus -> {
            if (likeStoryStatus.first) {
                observeNotNull(mineViewModel.getStory(storyId), myStoryListAdapter::updateRecord);
            }
        });
    }

    @Override
    public void onAuthorClick(String authorId) {}

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
        observeNotNull(mineViewModel.refreshMyStoryList(), myStory -> {
            binding.myStoryList.setRefreshing(false);
            myStoryListAdapter.setRecords(myStory);
            binding.swipeTarget.scrollToPosition(0);
            binding.myStoryList.setLoadMoreEnabled(myStory.size() >= MineViewModel.MY_STORY_LIST_PAGE_SIZE);
        });
    }

    private void autoRefresh() {
        binding.myStoryList.setRefreshing(true);
        onRefresh();
    }

    private void onLoadMore() {
        observeNotNull(mineViewModel.moreStoryList(), myStory -> {
            binding.myStoryList.setLoadingMore(false);
            myStoryListAdapter.addRecords(myStory);
            binding.myStoryList.setLoadMoreEnabled(myStory.size() >= MineViewModel.MY_STORY_LIST_PAGE_SIZE);
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
