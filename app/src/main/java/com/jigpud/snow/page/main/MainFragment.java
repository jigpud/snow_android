package com.jigpud.snow.page.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import bolts.Task;
import com.jigpud.snow.R;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.databinding.MainBinding;
import com.jigpud.snow.page.attractiondetail.AttractionDetailActivity;
import com.jigpud.snow.page.base.BaseFragment;
import com.jigpud.snow.page.common.adapter.HorizontalFoodListAdapter;
import com.jigpud.snow.page.common.adapter.RecommendAttractionListAdapter;
import com.jigpud.snow.page.common.itemdecoration.GridSpacingItemDecoration;
import com.jigpud.snow.page.common.itemdecoration.HorizontalSpacingItemDecoration;
import com.jigpud.snow.page.fooddetail.FoodDetailActivity;
import com.jigpud.snow.page.morefood.MoreFoodActivity;
import com.jigpud.snow.page.morerecommendattraction.MoreRecommendAttractionActivity;
import com.jigpud.snow.page.search.SearchActivity;
import com.jigpud.snow.util.constant.KeyConstant;
import com.jigpud.snow.util.pixel.PixelUtil;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.RectangleIndicator;

import java.util.List;

/**
 * @author : jigpud
 */
public class MainFragment extends BaseFragment<MainBinding> {
    private static final String TAG = "MainFragment";

    private MainViewModel mainViewModel;
    private RecommendAttractionListAdapter recommendAttractionListAdapter;
    private HorizontalFoodListAdapter foodListAdapter;
    private boolean isRefreshingHotAttraction;
    private boolean isRefreshingRecommendAttraction;
    private boolean isRefreshingFood;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainViewModel = getViewModel(MainViewModel.class, MainViewModelFactory.create());

        recommendAttractionListAdapter = new RecommendAttractionListAdapter(this::onAttractionClick);

        foodListAdapter = new HorizontalFoodListAdapter(this::onFoodClick);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        super.initView();

        binding.main.setOnRefreshListener(this::onRefresh);

        binding.searchBar.search.setKeyListener(null);
        binding.searchBar.search.setFocusable(false);
        binding.searchBar.search.setOnClickListener(this::onSearchBarClick);
        binding.searchBar.getRoot().setOnClickListener(this::onSearchBarClick);

        binding.foodList.setAdapter(foodListAdapter);
        binding.foodList.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.foodList.addItemDecoration(new HorizontalSpacingItemDecoration(8));
        binding.moreFood.setOnClickListener(this::onMoreFoodClick);

        binding.recommendAttractionList.setAdapter(recommendAttractionListAdapter);
        binding.recommendAttractionList.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.recommendAttractionList.addItemDecoration(new GridSpacingItemDecoration(10));
        binding.moreAttraction.setOnClickListener(this::onMoreRecommendAttractionClick);

        autoRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        useDarkStatusBar();
    }

    private void onMoreRecommendAttractionClick(View target) {
        Intent intent = new Intent(requireContext(), MoreRecommendAttractionActivity.class);
        startActivity(intent);
    }

    private void onMoreFoodClick(View target) {
        Intent intent = new Intent(requireContext(), MoreFoodActivity.class);
        startActivity(intent);
    }

    private void onFoodClick(String foodId) {
        Intent intent = new Intent(requireContext(), FoodDetailActivity.class);
        intent.putExtra(KeyConstant.KEY_FOOD_ID, foodId);
        startActivity(intent);
    }

    @SuppressWarnings("unchecked")
    private void onHotAttractionRefresh(List<AttractionEntity> hotAttractionList) {
        binding.banner.stop();
        binding.banner.addBannerLifecycleObserver(this)
                .setAdapter(new HotAttractionBannerAdapter(hotAttractionList, this::onAttractionClick))
                .setIndicator(new RectangleIndicator(requireContext()))
                .setIndicatorSelectedColor(ContextCompat.getColor(requireContext(), R.color.primary))
                .setIndicatorNormalColor(Color.WHITE)
                .setIndicatorHeight((int) PixelUtil.dpToPixel(3))
                .setIndicatorSelectedWidth((int) PixelUtil.dpToPixel(22))
                .setIndicatorNormalWidth((int) PixelUtil.dpToPixel(16))
                .setIndicatorSpace((int) PixelUtil.dpToPixel(8))
                .setIndicatorMargins(new IndicatorConfig.Margins(0, 0, 0, (int) PixelUtil.dpToPixel(5)))
                .isAutoLoop(true)
                .start();
    }

    private void onAttractionClick(String attractionId) {
        Intent intent = new Intent(requireContext(), AttractionDetailActivity.class);
        intent.putExtra(KeyConstant.KEY_ATTRACTION_ID, attractionId);
        startActivity(intent);
    }

    private void onRefresh() {
        isRefreshingHotAttraction = true;
        observeNotNull(mainViewModel.getHotAttractionList(), hotAttractionList -> {
            isRefreshingHotAttraction = false;
            updateRefreshState();
            onHotAttractionRefresh(hotAttractionList);
        });

        isRefreshingFood = true;
        observeNotNull(mainViewModel.getFoodList(), foodList -> {
            isRefreshingFood = false;
            updateRefreshState();
            foodListAdapter.setRecords(foodList);
        });

        isRefreshingRecommendAttraction = true;
        observeNotNull(mainViewModel.getRecommendAttractionList(), recommendAttractionList -> {
            isRefreshingRecommendAttraction = false;
            updateRefreshState();
            recommendAttractionListAdapter.setRecords(recommendAttractionList);
        });
    }

    private void autoRefresh() {
        binding.main.setRefreshing(true);
    }

    private void updateRefreshState() {
        if (!isRefreshingHotAttraction && !isRefreshingFood && !isRefreshingRecommendAttraction) {
            binding.main.setRefreshing(false);
        }
    }

    private void onSearchBarClick(View target) {
        startActivity(new Intent(requireContext(), SearchActivity.class));
    }

    private void useDarkStatusBar() {
        Task.call(() -> {
            setUseLightStatusBar(false);
            return null;
        }, Task.UI_THREAD_EXECUTOR);
    }

    @Override
    protected MainBinding createViewBinding(LayoutInflater inflater, ViewGroup container) {
        return MainBinding.inflate(inflater, container, false);
    }
}
