package com.jigpud.snow.page.userprofile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import bolts.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.UserProfileBinding;
import com.jigpud.snow.page.base.BaseActivity;
import com.jigpud.snow.page.common.adapter.StoryListAdapter;
import com.jigpud.snow.page.common.adapter.UserStoryListAdapter;
import com.jigpud.snow.page.common.widget.ScrollableSwipeToLoadLayout;
import com.jigpud.snow.page.storydetail.StoryDetailActivity;
import com.jigpud.snow.util.constant.KeyConstant;
import com.jigpud.snow.util.format.IntegerFormatter;
import com.jigpud.snow.util.img.ImageLoader;
import com.jigpud.snow.util.logger.Logger;
import com.jigpud.snow.util.user.CurrentUser;

/**
 * @author : jigpud
 */
public class UserProfileActivity extends BaseActivity<UserProfileBinding> implements StoryListAdapter.StoryClickListener,
        ScrollableSwipeToLoadLayout.ScrollableAdditionDetector {
    private static final String TAG = "UserProfileActivity";

    private UserProfileViewModel userProfileViewModel;
    private String userid = "";
    private boolean isGuestMode = true;
    private CollapsingToolbarLayoutState userProfileState;
    private UserStoryListAdapter userStoryListAdapter;

    @Override
    protected void preSetContent(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.preSetContent(savedInstanceState);
        userProfileState = CollapsingToolbarLayoutState.EXPANDED;
        updateStatusBar(userProfileState);
        userProfileViewModel = getViewModel(UserProfileViewModel.class, UserProfileViewModelFactory.create());

        Intent intent = getIntent();
        if (intent != null) {
            userid = intent.getStringExtra(KeyConstant.KEY_USERID);
            if (userid == null) {
                userid = "";
            }
        }
        isGuestMode = !userid.equals(CurrentUser.getInstance(getApplicationContext()).getCurrentUserid());

        userStoryListAdapter = new UserStoryListAdapter(UserProfileViewModel.USER_STORY_LIST_PAGE_SIZE, this);
    }

    @Override
    protected void initView() {
        super.initView();

        binding.back.setOnClickListener(target -> finish());

        binding.userProfile.addOnOffsetChangedListener(this::onUserProfileOffsetChanged);

        observeNotNull(userProfileViewModel.getUserInfo(userid), user -> {
            ImageLoader.loadImgFromUrl(
                    binding.background,
                    user.getBackground(),
                    R.drawable.ic_placeholder_user_profile_bg,
                    R.drawable.ic_placeholder_user_profile_bg
            );

            ImageLoader.loadImgFromUrl(
                    binding.avatar,
                    user.getAvatar(),
                    R.drawable.ic_placeholder_avatar,
                    R.drawable.ic_placeholder_avatar
            );

            binding.likesCount.setText(IntegerFormatter.formatWithUnit(user.getLikes()));

            binding.fansCount.setText(IntegerFormatter.formatWithUnit(user.getFollowers()));

            binding.followedCount.setText(IntegerFormatter.formatWithUnit(user.getFollowing()));
            int followButtonText = R.string.item_user_follow;
            if (user.isFollowed()) {
                followButtonText = R.string.item_user_unfollow;
            }
            binding.follow.setText(followButtonText);
            binding.follow.setOnClickListener(target -> {
                if (user.isFollowed()) {
                    onUnfollow(userid);
                } else {
                    onFollow(userid);
                }
            });
            if (isGuestMode) {
                binding.follow.setAlpha(1f);
                binding.follow.setEnabled(true);
            } else {
                binding.follow.setAlpha(0f);
                binding.follow.setEnabled(false);
            }

            binding.nickname.setText(user.getNickname());

            binding.signature.setText(user.getSignature());
        });

        binding.storyList.setScrollableAdditionDetector(this);
        binding.swipeTarget.setAdapter(userStoryListAdapter);
        binding.swipeTarget.setLayoutManager(new LinearLayoutManager(this));
        binding.swipeTarget.setItemAnimator(null);
        binding.storyList.setOnRefreshListener(this::onRefresh);
        binding.storyList.setOnLoadMoreListener(this::onLoadMore);

        autoRefresh();
    }

    @Override
    public void onStoryClick(String storyId) {
        Intent intent = new Intent(this, StoryDetailActivity.class);
        intent.putExtra(KeyConstant.KEY_STORY_ID, storyId);
        startActivity(intent);
    }

    @Override
    public void onLikeStory(String storyId) {
        observeNotNull(userProfileViewModel.likeStory(storyId), likeStoryStatus -> {
            if (likeStoryStatus.first) {
                observeNotNull(userProfileViewModel.getStory(storyId), userStoryListAdapter::updateRecord);
            }
        });
    }

    @Override
    public void onUnlikeStory(String storyId) {
        observeNotNull(userProfileViewModel.unlikeStory(storyId), likeStoryStatus -> {
            if (likeStoryStatus.first) {
                observeNotNull(userProfileViewModel.getStory(storyId), userStoryListAdapter::updateRecord);
            }
        });
    }

    @Override
    public void onAuthorClick(String authorId) {}

    @Override
    public boolean canChildScrollUp(View target) {
        Logger.d(TAG, "canChildScrollUp: %s", userProfileState.compareTo(CollapsingToolbarLayoutState.EXPANDED) != 0);
        return userProfileState.compareTo(CollapsingToolbarLayoutState.EXPANDED) != 0;
    }

    @Override
    public boolean canChildScrollDown(View target) {
        Logger.d(TAG, "canChildScrollDown: %s", userProfileState.compareTo(CollapsingToolbarLayoutState.COLLAPSED) != 0);
        return userProfileState.compareTo(CollapsingToolbarLayoutState.COLLAPSED) != 0;
    }

    private void onLoadMore() {
        observeNotNull(userProfileViewModel.moreUserStoryList(userid), userStoryList -> {
            binding.storyList.setLoadingMore(false);
            userStoryListAdapter.addRecords(userStoryList);
            binding.storyList.setLoadMoreEnabled(userStoryList.size() >= UserProfileViewModel.USER_STORY_LIST_PAGE_SIZE);
        });
    }

    private void onRefresh() {
        observeNotNull(userProfileViewModel.userStoryList(userid), userStoryList -> {
            binding.storyList.setRefreshing(false);
            userStoryListAdapter.setRecords(userStoryList);
            binding.swipeTarget.scrollToPosition(0);
            binding.storyList.setLoadMoreEnabled(userStoryList.size() >= UserProfileViewModel.USER_STORY_LIST_PAGE_SIZE);
        });
    }

    private void autoRefresh() {
        binding.storyList.setRefreshing(true);
    }

    private void onFollow(String userid) {
        observeNotNull(userProfileViewModel.follow(userid), followStatus -> {
            if (followStatus.first) {
                userProfileViewModel.getUserInfo(userid);
            }
        });
    }

    private void onUnfollow(String userid) {
        observeNotNull(userProfileViewModel.unfollow(userid), unfollowStatus -> {
            if (unfollowStatus.first) {
                userProfileViewModel.getUserInfo(userid);
            }
        });
    }

    private void onUserProfileOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            if (userProfileState != CollapsingToolbarLayoutState.EXPANDED) {
                userProfileState = CollapsingToolbarLayoutState.EXPANDED;
            }
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (userProfileState != CollapsingToolbarLayoutState.COLLAPSED) {
                userProfileState = CollapsingToolbarLayoutState.COLLAPSED;
            }
        } else {
            if (userProfileState != CollapsingToolbarLayoutState.DRAGGING) {
                userProfileState = CollapsingToolbarLayoutState.DRAGGING;
            }
        }
        onUserProfileExpandStateChanged(appBarLayout, verticalOffset, userProfileState);
    }

    private void onUserProfileExpandStateChanged(
            AppBarLayout appBarLayout,
            int verticalOffset,
            CollapsingToolbarLayoutState state
    ) {
        if (state == CollapsingToolbarLayoutState.COLLAPSED) {
            binding.header.setBackgroundResource(R.color.content_bg);
        } else {
            binding.header.setBackgroundColor(Color.TRANSPARENT);
        }
        binding.header.setClickable(state == CollapsingToolbarLayoutState.EXPANDED);
        updateStatusBar(state);
    }

    private void updateStatusBar(CollapsingToolbarLayoutState state) {
        Task.call(() -> {
            setUseLightStatusBar(state != CollapsingToolbarLayoutState.EXPANDED);
            return null;
        }, Task.UI_THREAD_EXECUTOR);
    }

    @Override
    protected UserProfileBinding createViewBinding() {
        return UserProfileBinding.inflate(getLayoutInflater());
    }

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        DRAGGING
    }
}
