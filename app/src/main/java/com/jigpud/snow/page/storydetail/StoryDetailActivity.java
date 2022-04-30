package com.jigpud.snow.page.storydetail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import bolts.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.StoryDetailBinding;
import com.jigpud.snow.page.base.BaseActivity;
import com.jigpud.snow.page.common.adapter.ImageAdapter;
import com.jigpud.snow.page.common.thumbinfo.ImageThumbViewInfo;
import com.jigpud.snow.page.common.widget.ScrollableSwipeToLoadLayout;
import com.jigpud.snow.page.userprofile.UserProfileActivity;
import com.jigpud.snow.util.constant.KeyConstant;
import com.jigpud.snow.util.format.DateFormatter;
import com.jigpud.snow.util.format.IntegerFormatter;
import com.jigpud.snow.util.img.ImageLoader;
import com.jigpud.snow.util.logger.Logger;
import com.jigpud.snow.util.user.CurrentUser;
import com.previewlibrary.GPreviewBuilder;
import com.youth.banner.indicator.BaseIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author : jigpud
 */
public class StoryDetailActivity extends BaseActivity<StoryDetailBinding> implements
        ScrollableSwipeToLoadLayout.ScrollableAdditionDetector, CommentListAdapter.CommentClickListener {
    private static final String TAG = "StoryDetailActivity";
    private static final String EMPTY_URL = "";

    private String storyId;
    private StoryDetailViewModel storyDetailViewModel;
    private ImageAdapter storyPictureListAdapter;
    private CollapsingToolbarLayoutState storyDetailState = CollapsingToolbarLayoutState.EXPANDED;
    private CommentListAdapter commentListAdapter;

    @Override
    protected void preSetContent(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.preSetContent(savedInstanceState);

        storyDetailViewModel = getViewModel(StoryDetailViewModel.class, StoryDetailViewModelFactory.create());

        Intent intent = getIntent();
        if (intent != null) {
            storyId = intent.getStringExtra(KeyConstant.KEY_STORY_ID);
            if (storyId == null) {
                storyId = "";
            }
        }

        commentListAdapter = new CommentListAdapter(StoryDetailViewModel.COMMENT_LIST_PAGE_SIZE, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initView() {
        super.initView();

        binding.back.setOnClickListener(target -> finish());

        observeNotNull(storyDetailViewModel.getStory(storyId), story -> {
            List<String> pictures = story.getPictures();
            if (pictures == null || pictures.isEmpty()) {
                pictures = new ArrayList<>();
                pictures.add(EMPTY_URL);
            }
            storyPictureListAdapter = new ImageAdapter(R.drawable.ic_placeholder_story_cover, pictures, this::onStoryPictureClick);
            binding.storyPictureList
                    .addBannerLifecycleObserver(this)
                    .isAutoLoop(false)
                    .setAdapter(storyPictureListAdapter)
                    .setIndicator(new BaseIndicator(this) {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onPageSelected(int position) {
                            binding.storyPictureListIndicator.setText(getStoryPictureIndicatorString(position));
                        }
                    });
            if (pictures.size() > 1) {
                binding.storyPictureListIndicator.setText(getStoryPictureIndicatorString(0));
                binding.storyPictureListIndicator.setAlpha(View.VISIBLE);
            } else {
                binding.storyPictureListIndicator.setVisibility(View.GONE);
            }

            int favoriteColor = ContextCompat.getColor(this, R.color.text_dark_light);
            if (story.isHaveFavorite()) {
                favoriteColor = ContextCompat.getColor(this, R.color.primary);
            }
            binding.favorite.setIconTint(ColorStateList.valueOf(favoriteColor));
            binding.favorite.setOnClickListener(target -> {
                if (story.isHaveFavorite()) {
                    onUnFavoriteStory(story.getStoryId());
                } else {
                    onFavoriteStory(story.getStoryId());
                }
            });

            String currentUserid = CurrentUser.getInstance(this).getCurrentUserid();
            if (currentUserid != null && currentUserid.equals(story.getAuthorId())) {
                binding.authorAvatar.setVisibility(View.GONE);
                binding.authorNicknameContainer.setVisibility(View.GONE);
            } else {
                ImageLoader.loadImgFromUrl(
                        binding.authorAvatar,
                        story.getAuthorAvatar(),
                        R.drawable.ic_placeholder_avatar,
                        R.drawable.ic_placeholder_avatar
                );
                binding.authorNickname.setText(story.getAuthorNickname());
                binding.authorAvatar.setVisibility(View.VISIBLE);
                binding.authorNicknameContainer.setVisibility(View.VISIBLE);
            }

            binding.title.setText(story.getTitle());

            int likesColor = ContextCompat.getColor(this, R.color.text_dark_light);
            if (story.isLiked()) {
                likesColor = ContextCompat.getColor(this, R.color.primary);
            }
            binding.likes.setIconTint(ColorStateList.valueOf(likesColor));
            binding.likes.setOnClickListener(target -> {
                if (story.isLiked()) {
                    onUnlikeStory();
                } else {
                    onLikeStory();
                }
            });
            binding.likesCount.setText(IntegerFormatter.formatWithUnit(story.getLikes()));
            binding.likesCount.setTextColor(likesColor);

            binding.releaseLocation.setText(story.getReleaseLocation());

            binding.releaseTime.setText(DateFormatter.yearMonthDayHourMinute(story.getReleaseTime()));

            binding.content.setText(story.getContent());
        });

        binding.storyDetail.addOnOffsetChangedListener(this::onStoryDetailOffsetChanged);

        binding.storyCommentList.setScrollableAdditionDetector(this);

        binding.storyCommentList.setOnRefreshListener(this::onRefresh);
        binding.storyCommentList.setOnLoadMoreListener(this::onLoadMore);
        binding.swipeTarget.setLayoutManager(new LinearLayoutManager(this));
        binding.swipeTarget.setAdapter(commentListAdapter);
        binding.swipeTarget.setItemAnimator(null);

        binding.commentInput.addTextChangedListener(onCommentInputChanged());
        binding.postComment.setOnClickListener(this::onPostCommentClick);

        autoRefresh();
    }

    @Override
    public boolean canChildScrollUp(View target) {
        Logger.d(TAG, "canChildScrollUp: %s", storyDetailState.compareTo(CollapsingToolbarLayoutState.EXPANDED) != 0);
        return storyDetailState.compareTo(CollapsingToolbarLayoutState.EXPANDED) != 0;
    }

    @Override
    public boolean canChildScrollDown(View target) {
        Logger.d(TAG, "canChildScrollDown: %s", storyDetailState.compareTo(CollapsingToolbarLayoutState.COLLAPSED) != 0);
        return storyDetailState.compareTo(CollapsingToolbarLayoutState.COLLAPSED) != 0;
    }

    @Override
    public void onAuthorClick(String userid) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra(KeyConstant.KEY_USERID, userid);
        startActivity(intent);
    }

    @Override
    public void onLikeComment(String commentId) {
        observeNotNull(storyDetailViewModel.likeComment(commentId), likeStoryStatus -> {
            if (likeStoryStatus.first) {
                observeNotNull(storyDetailViewModel.getComment(commentId), comment -> commentListAdapter.updateRecord(comment));
            }
        });
    }

    @Override
    public void onUnlikeComment(String commentId) {
        observeNotNull(storyDetailViewModel.unlikeComment(commentId), unlikeCommentStatus -> {
            if (unlikeCommentStatus.first) {
                observeNotNull(storyDetailViewModel.getComment(commentId), comment -> commentListAdapter.updateRecord(comment));
            }
        });
    }

    private void onFavoriteStory(String storyId) {
        observeNotNull(storyDetailViewModel.favoriteStory(storyId), favoriteStoryStatus -> {
            if (favoriteStoryStatus.first) {
                storyDetailViewModel.getStory(storyId);
            }
        });
    }

    private void onUnFavoriteStory(String storyId) {
        observeNotNull(storyDetailViewModel.unFavoriteStory(storyId), unFavoriteStoryStatus -> {
            if (unFavoriteStoryStatus.first) {
                storyDetailViewModel.getStory(storyId);
            }
        });
    }

    private void onPostCommentClick(View target) {
        String content = Objects.requireNonNull(binding.commentInput.getText()).toString();
        loading();
        observeNotNull(storyDetailViewModel.postComment(storyId, content), postCommentStatus -> {
            unLoading();
            if (postCommentStatus.first) {
                Logger.d(TAG, "post comment success");
                binding.commentInput.clearFocus();
                binding.commentInput.setText("");
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                autoRefresh();
            } else {
                Logger.d(TAG, "post comment failed! cause by %s", postCommentStatus.second);
            }
        });
    }

    private TextWatcher onCommentInputChanged() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updatePostCommentButtonEnabled();
            }
        };
    }

    private void onStoryPictureClick(int position) {
        List<String> storyPictureList = storyPictureListAdapter.getImageUrlList();
        if (storyPictureList.size() == 1 && EMPTY_URL.equals(storyPictureList.get(0))) {
            return;
        }
        List<ImageThumbViewInfo> data = new ArrayList<>();
        for (String url : storyPictureList) {
            data.add(new ImageThumbViewInfo(url));
        }
        GPreviewBuilder.from(this)
                .setData(data)
                .setCurrentIndex(position)
                .setSingleFling(false)
                .isDisableDrag(true)
                .start();
    }

    private void onRefresh() {
        observeNotNull(storyDetailViewModel.refreshCommentList(storyId), commentList -> {
            binding.storyCommentList.setRefreshing(false);
            commentListAdapter.setRecords(commentList);
            binding.storyCommentList.setLoadMoreEnabled(commentList.size() >= StoryDetailViewModel.COMMENT_LIST_PAGE_SIZE);
        });
    }

    private void autoRefresh() {
        binding.storyCommentList.setRefreshing(true);
    }

    private void onLoadMore() {
        observeNotNull(storyDetailViewModel.moreCommentList(storyId), commentList -> {
            binding.storyCommentList.setLoadingMore(false);
            commentListAdapter.setRecords(commentList);
            binding.storyCommentList.setLoadMoreEnabled(commentList.size() >= StoryDetailViewModel.COMMENT_LIST_PAGE_SIZE);
        });
    }

    private void onLikeStory() {
        observeNotNull(storyDetailViewModel.likeStory(storyId), likeStoryStatus -> {
            if (likeStoryStatus.first) {
                storyDetailViewModel.getStory(storyId);
            }
        });
    }

    private void onUnlikeStory()  {
        observeNotNull(storyDetailViewModel.unlikeStory(storyId), unlikeStoryStatus -> {
            if (unlikeStoryStatus.first) {
                storyDetailViewModel.getStory(storyId);
            }
        });
    }

    private void onStoryDetailOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            if (storyDetailState != CollapsingToolbarLayoutState.EXPANDED) {
                storyDetailState = CollapsingToolbarLayoutState.EXPANDED;
            }
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (storyDetailState != CollapsingToolbarLayoutState.COLLAPSED) {
                storyDetailState = CollapsingToolbarLayoutState.COLLAPSED;
            }
        } else {
            if (storyDetailState != CollapsingToolbarLayoutState.DRAGGING) {
                storyDetailState = CollapsingToolbarLayoutState.DRAGGING;
            }
        }
        onStoryDetailExpandStateChanged(appBarLayout, verticalOffset, storyDetailState);
    }

    private void onStoryDetailExpandStateChanged(
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
            setUseLightStatusBar(state != CollapsingToolbarLayoutState.EXPANDED);
            return null;
        }, Task.UI_THREAD_EXECUTOR);
    }

    private String getStoryPictureIndicatorString(int position) {
        return IntegerFormatter.formatFraction(position + 1, storyPictureListAdapter.getImageUrlList().size());
    }

    private void updatePostCommentButtonEnabled() {
        boolean enabled = Objects.requireNonNull(binding.commentInput.getText()).length() > 0;
        binding.postComment.setEnabled(enabled);
    }

    @Override
    protected StoryDetailBinding createViewBinding() {
        return StoryDetailBinding.inflate(getLayoutInflater());
    }

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        DRAGGING
    }
}
