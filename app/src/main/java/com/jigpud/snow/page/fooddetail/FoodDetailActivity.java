package com.jigpud.snow.page.fooddetail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import bolts.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.FoodDetailBinding;
import com.jigpud.snow.page.attractiondetail.AttractionDetailActivity;
import com.jigpud.snow.page.base.BaseActivity;
import com.jigpud.snow.page.common.adapter.AttractionListAdapter;
import com.jigpud.snow.page.common.adapter.ImageAdapter;
import com.jigpud.snow.page.common.itemdecoration.GridSpacingItemDecoration;
import com.jigpud.snow.page.common.thumbinfo.ImageThumbViewInfo;
import com.jigpud.snow.page.common.widget.ScrollableSwipeToLoadLayout;
import com.jigpud.snow.util.constant.KeyConstant;
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
public class FoodDetailActivity extends BaseActivity<FoodDetailBinding> implements
        ScrollableSwipeToLoadLayout.ScrollableAdditionDetector {
    private static final String TAG = "FoodDetailActivity";
    private static final String EMPTY_URL = "";
    
    private String foodId;
    private FoodDetailViewModel foodDetailViewModel;
    private CollapsingToolbarLayoutState foodDetailState;
    private ImageAdapter foodPictureListBannerAdapter;
    private FoodAttractionListAdapter foodAttractionListAdapter;
    private boolean isRefreshingFoodInfo;
    private boolean isRefreshingAttractionList;

    @Override
    protected void preSetContent(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.preSetContent(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            foodId = intent.getStringExtra(KeyConstant.KEY_FOOD_ID);
        }

        foodDetailViewModel = getViewModel(FoodDetailViewModel.class, FoodDetailViewModelFactory.create());

        foodDetailState = CollapsingToolbarLayoutState.EXPANDED;

        foodAttractionListAdapter = new FoodAttractionListAdapter(FoodDetailViewModel.ATTRACTION_LIST_PAGE_SIZE, this::onAttractionClick);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initView() {
        super.initView();

        binding.foodDetail.setOnLoadMoreListener(this::onLoadMore);
        binding.foodDetail.setOnRefreshListener(this::onRefresh);
        binding.foodDetail.setLoadMoreEnabled(false);
        binding.foodDetail.setScrollableAdditionDetector(this);

        binding.foodInfo.addOnOffsetChangedListener(this::onFoodDetailOffsetChanged);

        observeNotNull(foodDetailViewModel.getFood(foodId), food -> {
            List<String> pictureList = new ArrayList<>(food.getPictures());
            if (pictureList.isEmpty()) {
                pictureList.add(EMPTY_URL);
            }
            foodPictureListBannerAdapter = new ImageAdapter(
                    R.drawable.ic_placeholder_food_cover, pictureList, this::onPictureClick);
            binding.foodPictureList.stop();
            binding.foodPictureList
                    .addBannerLifecycleObserver(this)
                    .setAdapter(foodPictureListBannerAdapter)
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

            binding.name.setText(food.getName());

            binding.description.setText(food.getDescription());
        });

        binding.attractionList.setAdapter(foodAttractionListAdapter);
        binding.attractionList.setLayoutManager(new LinearLayoutManager(this));
        binding.attractionList.addItemDecoration(new GridSpacingItemDecoration(12));

        autoRefresh();
    }

    @Override
    public boolean canChildScrollUp(View target) {
        boolean canChildScrollUp = foodDetailState.compareTo(CollapsingToolbarLayoutState.EXPANDED) != 0;
        Logger.d(TAG, "canChildScrollUp: %s", canChildScrollUp);
        return canChildScrollUp;
    }

    @Override
    public boolean canChildScrollDown(View target) {
        boolean canChildScrollDown = foodDetailState.compareTo(CollapsingToolbarLayoutState.COLLAPSED) != 0;
        Logger.d(TAG, "canChildScrollDown: %s", canChildScrollDown);
        return canChildScrollDown;
    }

    private void onRefresh() {
        refreshFoodInfo();
        refreshAttractionList();
    }

    private void refreshFoodInfo() {
        isRefreshingFoodInfo = true;
        observeNotNull(foodDetailViewModel.getFood(foodId), food -> {
            isRefreshingFoodInfo = false;
            updateRefreshState();
        });
    }

    private void refreshAttractionList() {
        isRefreshingAttractionList = true;
        observeNotNull(foodDetailViewModel.refreshAttractionList(foodId), attractionList -> {
            isRefreshingAttractionList = false;
            updateRefreshState();
            foodAttractionListAdapter.setRecords(attractionList);
            binding.attractionList.scrollToPosition(0);
            binding.foodDetail.setLoadMoreEnabled(attractionList.size() >= FoodDetailViewModel.ATTRACTION_LIST_PAGE_SIZE);
        });
    }

    private void autoRefresh() {
        binding.foodDetail.setRefreshing(true);
    }

    private void updateRefreshState() {
        if (!isRefreshingFoodInfo && !isRefreshingAttractionList) {
            binding.foodDetail.setRefreshing(false);
        }
    }

    private void onLoadMore() {
        observeNotNull(foodDetailViewModel.loadMoreAttractionList(foodId), attractionList -> {
            binding.foodDetail.setLoadingMore(false);
            foodAttractionListAdapter.addRecords(attractionList);
            binding.foodDetail.setLoadMoreEnabled(attractionList.size() >= FoodDetailViewModel.ATTRACTION_LIST_PAGE_SIZE);
        });
    }

    private void onAttractionClick(String attractionId) {
        Intent intent = new Intent(this, AttractionDetailActivity.class);
        intent.putExtra(KeyConstant.KEY_ATTRACTION_ID, attractionId);
        startActivity(intent);
    }

    private void onPictureClick(int position) {
        List<String> pictureList = new ArrayList<>(foodPictureListBannerAdapter.getImageUrlList());
        if (pictureList.size() == 1 && EMPTY_URL.equals(pictureList.get(0))) {
            return;
        }
        List<ImageThumbViewInfo> data = new ArrayList<>();
        for (String picture : pictureList) {
            data.add(new ImageThumbViewInfo(picture));
        }
        GPreviewBuilder.from(this)
                .setData(data)
                .setCurrentIndex(position)
                .setSingleFling(false)
                .isDisableDrag(true)
                .start();
    }

    private void onFoodDetailOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            if (foodDetailState != CollapsingToolbarLayoutState.EXPANDED) {
                foodDetailState = CollapsingToolbarLayoutState.EXPANDED;
            }
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (foodDetailState != CollapsingToolbarLayoutState.COLLAPSED) {
                foodDetailState = CollapsingToolbarLayoutState.COLLAPSED;
            }
        } else {
            if (foodDetailState != CollapsingToolbarLayoutState.DRAGGING) {
                foodDetailState = CollapsingToolbarLayoutState.DRAGGING;
            }
        }
        onFoodDetailExpandStateChanged(appBarLayout, verticalOffset, foodDetailState);
    }

    private void onFoodDetailExpandStateChanged(
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

    @Override
    protected FoodDetailBinding createViewBinding() {
        return FoodDetailBinding.inflate(getLayoutInflater());
    }

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        DRAGGING
    }
}
