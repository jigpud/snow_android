package com.jigpud.snow.page.morefood;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.jigpud.snow.databinding.MoreFoodBinding;
import com.jigpud.snow.page.base.BaseActivity;
import com.jigpud.snow.page.common.adapter.VerticalFoodListAdapter;
import com.jigpud.snow.page.common.itemdecoration.GridSpacingItemDecoration;
import com.jigpud.snow.page.fooddetail.FoodDetailActivity;
import com.jigpud.snow.util.constant.KeyConstant;

/**
 * @author : jigpud
 */
public class MoreFoodActivity extends BaseActivity<MoreFoodBinding> {
    private MoreFoodViewModel moreFoodViewModel;
    private VerticalFoodListAdapter foodListAdapter;

    @Override
    protected void preSetContent(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.preSetContent(savedInstanceState);

        moreFoodViewModel = getViewModel(MoreFoodViewModel.class, MoreFoodViewModelFactory.create());

        foodListAdapter = new VerticalFoodListAdapter(MoreFoodViewModel.FOOD_LIST_PAGE_SIZE, this::onFoodClick);
    }

    @Override
    protected void initView() {
        super.initView();

        binding.back.setOnClickListener(target -> finish());

        binding.foodList.setOnRefreshListener(this::onRefresh);
        binding.foodList.setOnLoadMoreListener(this::onLoadMore);
        binding.foodList.setLoadMoreEnabled(false);
        binding.swipeTarget.setLayoutManager(new LinearLayoutManager(this));
        binding.swipeTarget.setAdapter(foodListAdapter);
        binding.swipeTarget.addItemDecoration(new GridSpacingItemDecoration(12));

        autoRefresh();
    }

    private void onFoodClick(String foodId) {
        Intent intent = new Intent(this, FoodDetailActivity.class);
        intent.putExtra(KeyConstant.KEY_FOOD_ID, foodId);
        startActivity(intent);
    }

    private void onRefresh() {
        observeNotNull(moreFoodViewModel.refreshFoodList(), foodList -> {
            binding.foodList.setRefreshing(false);
            foodListAdapter.setRecords(foodList);
            binding.swipeTarget.scrollToPosition(0);
            binding.foodList.setLoadMoreEnabled(foodList.size() >= MoreFoodViewModel.FOOD_LIST_PAGE_SIZE);
        });
    }

    private void autoRefresh() {
        binding.foodList.setRefreshing(true);
    }

    private void onLoadMore() {
        observeNotNull(moreFoodViewModel.loadMoreFoodList(), foodList -> {
            binding.foodList.setLoadingMore(false);
            foodListAdapter.addRecords(foodList);
            binding.foodList.setLoadMoreEnabled(foodList.size() >= MoreFoodViewModel.FOOD_LIST_PAGE_SIZE);
        });
    }

    @Override
    protected MoreFoodBinding createViewBinding() {
        return MoreFoodBinding.inflate(getLayoutInflater());
    }
}
