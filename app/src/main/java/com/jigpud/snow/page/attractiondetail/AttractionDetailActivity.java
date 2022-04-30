package com.jigpud.snow.page.attractiondetail;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bolts.Task;
import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.AttractionDetailBinding;
import com.jigpud.snow.databinding.ScoreAttractionBinding;
import com.jigpud.snow.page.attractionphotolist.AttractionPhotoListActivity;
import com.jigpud.snow.page.base.BaseActivity;
import com.jigpud.snow.page.common.adapter.ImageAdapter;
import com.jigpud.snow.page.common.adapter.StoryListAdapter;
import com.jigpud.snow.page.common.thumbinfo.ImageThumbViewInfo;
import com.jigpud.snow.page.common.widget.ScrollableSwipeToLoadLayout;
import com.jigpud.snow.page.storydetail.StoryDetailActivity;
import com.jigpud.snow.page.userprofile.UserProfileActivity;
import com.jigpud.snow.util.constant.KeyConstant;
import com.jigpud.snow.util.format.AttractionFormatter;
import com.jigpud.snow.util.format.FloatFormatter;
import com.jigpud.snow.util.logger.Logger;
import com.jigpud.snow.util.pixel.PixelUtil;
import com.previewlibrary.GPreviewBuilder;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.RectangleIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public class AttractionDetailActivity extends BaseActivity<AttractionDetailBinding> implements
        ScrollableSwipeToLoadLayout.ScrollableAdditionDetector, StoryListAdapter.StoryClickListener {
    private static final String TAG = "AttractionDetailActivity";
    private static final String EMPTY_URL = "";
    
    private AttractionDetailViewModel attractionDetailViewModel;
    private String attractionId;
    private ImageAdapter attractionPhotoListBannerAdapter;
    private AttractionTagListAdapter attractionTagListAdapter;
    private CollapsingToolbarLayoutState attractionDetailState;
    private AttractionStoryListAdapter attractionStoryListAdapter;
    private int newScore = 0;

    @Override
    protected void preSetContent(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.preSetContent(savedInstanceState);

        attractionDetailViewModel = getViewModel(AttractionDetailViewModel.class, AttractionDetailViewModelFactory.create());

        Intent intent = getIntent();
        if (intent != null) {
            attractionId = intent.getStringExtra(KeyConstant.KEY_ATTRACTION_ID);
        } else {
            attractionId = "";
        }

        attractionTagListAdapter = new AttractionTagListAdapter();
        
        attractionDetailState = CollapsingToolbarLayoutState.EXPANDED;

        attractionStoryListAdapter = new AttractionStoryListAdapter(AttractionDetailViewModel.STORY_LIST_PAGE_SIZE, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initView() {
        super.initView();

        binding.back.setOnClickListener(target -> finish());

        binding.tagList.setAdapter(attractionTagListAdapter);
        binding.tagList.setLayoutManager(new FlexboxLayoutManager(this));
        binding.tagList.addItemDecoration(attractionTagItemDecoration());
        binding.tagList.setItemAnimator(null);

        observeNotNull(attractionDetailViewModel.getAttraction(attractionId), attraction -> {
            List<String> photoList = new ArrayList<>(attraction.getPhotos());
            if (photoList.isEmpty()) {
                photoList.add(EMPTY_URL);
            }
            attractionPhotoListBannerAdapter = new ImageAdapter(R.drawable.ic_placeholder_attraction_cover,
                    photoList, this::onPhotoClick);
            binding.attractionPhotoList.stop();
            binding.attractionPhotoList
                    .addBannerLifecycleObserver(this)
                    .setAdapter(attractionPhotoListBannerAdapter)
                    .setIndicator(new RectangleIndicator(this))
                    .setIndicatorSelectedColor(ContextCompat.getColor(this, R.color.primary))
                    .setIndicatorNormalColor(Color.WHITE)
                    .setIndicatorHeight((int) PixelUtil.dpToPixel(3))
                    .setIndicatorSelectedWidth((int) PixelUtil.dpToPixel(22))
                    .setIndicatorNormalWidth((int) PixelUtil.dpToPixel(16))
                    .setIndicatorSpace((int) PixelUtil.dpToPixel(8))
                    .setIndicatorMargins(new IndicatorConfig.Margins(0, 0, 0, (int) PixelUtil.dpToPixel(40)))
                    .isAutoLoop(true)
                    .start();

            if (attraction.getPhotos().size() > 5) {
                binding.attractionPhotoCount.setText(AttractionFormatter.formatPhotoCount(attraction.getPhotos().size()));
                binding.attractionPhotoCount.setVisibility(View.VISIBLE);
            } else {
                binding.attractionPhotoCount.setVisibility(View.GONE);
            }

            if (!attraction.getPhotos().isEmpty()) {
                binding.moreAttractionPhoto.setOnClickListener(this::onMorePhotoListClick);
                binding.moreAttractionPhoto.setVisibility(View.VISIBLE);
            } else {
                binding.moreAttractionPhoto.setVisibility(View.GONE);
            }

            binding.name.setText(attraction.getName());

            binding.location.setText(attraction.getLocation());

            binding.description.setText(attraction.getDescription());

            binding.scoreContainer.setOnClickListener(target -> onScoreClick(attraction.getScore(), attraction.getMyScore()));
            binding.score.setText(FloatFormatter.formatWithDotOne(attraction.getScore()));
            binding.scoreCount.setText(AttractionFormatter.formatScoreCount(attraction.getScoreCount()));

            int followColor = ContextCompat.getColor(this, R.color.text_dark_light);
            if (attraction.isFollowed()) {
                followColor = ContextCompat.getColor(this, R.color.primary);
            }
            binding.follow.setColorFilter(followColor);
            binding.followContainer.setOnClickListener(target -> {
                if (attraction.isFollowed()) {
                    onUnfollowAttraction();
                } else {
                    onFollowAttraction();
                }
            });

            attractionTagListAdapter.setRecords(attraction.getTags());
        });

        binding.attractionDetail.addOnOffsetChangedListener(this::onAttractionDetailOffsetChanged);

        binding.refreshAttractionStoryList.setOnClickListener(this::onRefreshAttractionStoryListClick);
        binding.storyList.setScrollableAdditionDetector(this);
        binding.storyList.setOnLoadMoreListener(this::onLoadMore);
        binding.storyList.setLoadMoreEnabled(false);
        binding.swipeTarget.setLayoutManager(new LinearLayoutManager(this));
        binding.swipeTarget.setAdapter(attractionStoryListAdapter);
        binding.swipeTarget.setItemAnimator(null);

        refreshAttractionStoryList();
    }

    @Override
    public boolean canChildScrollUp(View target) {
        boolean canChildScrollUp = attractionDetailState.compareTo(CollapsingToolbarLayoutState.EXPANDED) != 0;
        Logger.d(TAG, "canChildScrollUp: %s", canChildScrollUp);
        return canChildScrollUp;
    }

    @Override
    public boolean canChildScrollDown(View target) {
        boolean canChildScrollDown = attractionDetailState.compareTo(CollapsingToolbarLayoutState.COLLAPSED) != 0;
        Logger.d(TAG, "canChildScrollDown: %s", canChildScrollDown);
        return canChildScrollDown;
    }

    @Override
    public void onStoryClick(String storyId) {
        Intent intent = new Intent(this, StoryDetailActivity.class);
        intent.putExtra(KeyConstant.KEY_STORY_ID, storyId);
        startActivity(intent);
    }

    @Override
    public void onLikeStory(String storyId) {
        observeNotNull(attractionDetailViewModel.likeStory(storyId), likeStoryStatus -> {
            if (likeStoryStatus.first) {
                observeNotNull(attractionDetailViewModel.getStory(storyId), attractionStoryListAdapter::updateRecord);
            }
        });
    }

    @Override
    public void onUnlikeStory(String storyId) {
        observeNotNull(attractionDetailViewModel.unlikeStory(storyId), unlikeStoryStatus -> {
            if (unlikeStoryStatus.first) {
                observeNotNull(attractionDetailViewModel.getStory(storyId), attractionStoryListAdapter::updateRecord);
            }
        });
    }

    @Override
    public void onAuthorClick(String authorId) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra(KeyConstant.KEY_USERID, authorId);
        startActivity(intent);
    }

    private void onRefreshAttractionStoryListClick(View target) {
        refreshAttractionStoryList();
    }

    private void refreshAttractionStoryList() {
        startRefreshAnimation();
        observeNotNull(attractionDetailViewModel.refreshAttractionStoryList(attractionId), attractionStoryList -> {
            stopRefreshAnimation();
            attractionStoryListAdapter.setRecords(attractionStoryList);
            binding.storyList.setLoadMoreEnabled(attractionStoryList.size() >= AttractionDetailViewModel.STORY_LIST_PAGE_SIZE);
            binding.swipeTarget.scrollToPosition(0);
        });
    }

    private void onLoadMore() {
        observeNotNull(attractionDetailViewModel.moreAttractionStoryList(attractionId), attractionStoryList -> {
            binding.storyList.setLoadingMore(false);
            attractionStoryListAdapter.addRecords(attractionStoryList);
            binding.storyList.setLoadMoreEnabled(attractionStoryList.size() >= AttractionDetailViewModel.STORY_LIST_PAGE_SIZE);
        });
    }

    private RecyclerView.ItemDecoration attractionTagItemDecoration() {
        FlexboxItemDecoration flexboxItemDecoration = new FlexboxItemDecoration(this);
        flexboxItemDecoration.setDrawable(
                ContextCompat.getDrawable(this, R.drawable.shape_attraction_detail_tag_item_divider));
        return flexboxItemDecoration;
    }

    private void onScoreClick(float currentScore, float myScore) {
        ScoreAttractionBinding scoreAttractionBinding = ScoreAttractionBinding.inflate(getLayoutInflater());
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(this)
                .setView(scoreAttractionBinding.getRoot())
                .setOnCancelListener(this::onCancelScore)
                .show();
        scoreAttractionBinding.score.setText(FloatFormatter.formatWithDotOne(currentScore));
        setScoreStar(scoreAttractionBinding, myScore);

        scoreAttractionBinding.scoreOne.setOnClickListener(target -> onScore(scoreAttractionBinding, target));
        scoreAttractionBinding.scoreTwo.setOnClickListener(target -> onScore(scoreAttractionBinding, target));
        scoreAttractionBinding.scoreThree.setOnClickListener(target -> onScore(scoreAttractionBinding, target));
        scoreAttractionBinding.scoreFour.setOnClickListener(target -> onScore(scoreAttractionBinding, target));
        scoreAttractionBinding.scoreFive.setOnClickListener(target -> onScore(scoreAttractionBinding, target));

        scoreAttractionBinding.cancel.setOnClickListener(target -> onCancelScore(alertDialog));
        scoreAttractionBinding.confirm.setOnClickListener(target -> onScoreConfirm(alertDialog));
    }

    @SuppressLint("NonConstantResourceId")
    private void onScore(ScoreAttractionBinding scoreAttractionBinding, View target) {
        switch (target.getId()) {
            case R.id.score_one:
                newScore = 1;
                break;
            case R.id.score_two:
                newScore = 2;
                break;
            case R.id.score_three:
                newScore = 3;
                break;
            case R.id.score_four:
                newScore = 4;
                break;
            case R.id.score_five:
                newScore = 5;
                break;
        }
        setScoreStar(scoreAttractionBinding, newScore);
    }

    private void setScoreStar(ScoreAttractionBinding scoreAttractionBinding, float score) {
        List<ColorStateList> scoreStarColorList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (i < score) {
                scoreStarColorList.add(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary)));
            } else {
                scoreStarColorList.add(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.text_dark_light)));
            }
        }
        scoreAttractionBinding.scoreOne.setIconTint(scoreStarColorList.get(0));
        scoreAttractionBinding.scoreTwo.setIconTint(scoreStarColorList.get(1));
        scoreAttractionBinding.scoreThree.setIconTint(scoreStarColorList.get(2));
        scoreAttractionBinding.scoreFour.setIconTint(scoreStarColorList.get(3));
        scoreAttractionBinding.scoreFive.setIconTint(scoreStarColorList.get(4));
    }

    private void onCancelScore(DialogInterface dialog) {
        newScore = 0;
        dialog.dismiss();
    }

    private void onScoreConfirm(DialogInterface dialog) {
        observeNotNull(attractionDetailViewModel.scoreAttraction(attractionId, newScore), scoreAttractionStatus -> {
            if (scoreAttractionStatus.first) {
                attractionDetailViewModel.getAttraction(attractionId);
            }
        });
        newScore = 0;
        dialog.dismiss();
    }

    private void onFollowAttraction() {
        observeNotNull(attractionDetailViewModel.followAttraction(attractionId), followAttractionStatus -> {
            if (followAttractionStatus.first) {
                attractionDetailViewModel.getAttraction(attractionId);
            }
        });
    }

    private void onUnfollowAttraction() {
        observeNotNull(attractionDetailViewModel.unfollowAttraction(attractionId), unfollowAttractionStatus -> {
            if (unfollowAttractionStatus.first) {
                attractionDetailViewModel.getAttraction(attractionId);
            }
        });
    }

    private void onMorePhotoListClick(View target) {
        Intent intent = new Intent(this, AttractionPhotoListActivity.class);
        intent.putExtra(KeyConstant.KEY_ATTRACTION_ID, attractionId);
        startActivity(intent);
    }

    private void onPhotoClick(int position) {
        List<String> photoList = new ArrayList<>(attractionPhotoListBannerAdapter.getImageUrlList());
        if (photoList.size() == 1 && EMPTY_URL.equals(photoList.get(0))) {
            return;
        }
        List<ImageThumbViewInfo> data = new ArrayList<>();
        for (String photo : photoList) {
            data.add(new ImageThumbViewInfo(photo));
        }
        GPreviewBuilder.from(this)
                .setData(data)
                .setCurrentIndex(position)
                .setSingleFling(false)
                .isDisableDrag(true)
                .start();
    }

    private void onAttractionDetailOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            if (attractionDetailState != CollapsingToolbarLayoutState.EXPANDED) {
                attractionDetailState = CollapsingToolbarLayoutState.EXPANDED;
            }
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (attractionDetailState != CollapsingToolbarLayoutState.COLLAPSED) {
                attractionDetailState = CollapsingToolbarLayoutState.COLLAPSED;
            }
        } else {
            if (attractionDetailState != CollapsingToolbarLayoutState.DRAGGING) {
                attractionDetailState = CollapsingToolbarLayoutState.DRAGGING;
            }
        }
        onAttractionDetailExpandStateChanged(appBarLayout, verticalOffset, attractionDetailState);
    }

    private void onAttractionDetailExpandStateChanged(
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

    private void startRefreshAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_refresh_btn);
        binding.refreshAttractionStoryList.startAnimation(animation);
    }

    private void stopRefreshAnimation() {
        binding.refreshAttractionStoryList.clearAnimation();
    }

    @Override
    protected AttractionDetailBinding createViewBinding() {
        return AttractionDetailBinding.inflate(getLayoutInflater());
    }

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        DRAGGING
    }
}
