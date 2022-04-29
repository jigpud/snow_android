package com.jigpud.snow.page.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import bolts.Task;
import com.jigpud.snow.databinding.MainBinding;
import com.jigpud.snow.page.attractiondetail.AttractionDetailActivity;
import com.jigpud.snow.page.base.BaseFragment;
import com.jigpud.snow.page.common.adapter.RecommendAttractionListAdapter;
import com.jigpud.snow.page.common.itemdecoration.GridSpacingItemDecoration;
import com.jigpud.snow.page.search.SearchActivity;
import com.jigpud.snow.util.constant.KeyConstant;

/**
 * @author : jigpud
 */
public class MainFragment extends BaseFragment<MainBinding> {
    private static final String TAG = "MainFragment";

    private MainViewModel mainViewModel;
    private RecommendAttractionListAdapter recommendAttractionListAdapter;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = getViewModel(MainViewModel.class, MainViewModelFactory.create());
        recommendAttractionListAdapter = new RecommendAttractionListAdapter(this::onAttractionClick);
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

        binding.recommendAttractionList.setAdapter(recommendAttractionListAdapter);
        binding.recommendAttractionList.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.recommendAttractionList.addItemDecoration(new GridSpacingItemDecoration(10));

        autoRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        useLightStatusBar();
    }

    private void onAttractionClick(String attractionId) {
        Intent intent = new Intent(requireContext(), AttractionDetailActivity.class);
        intent.putExtra(KeyConstant.KEY_ATTRACTION_ID, attractionId);
        startActivity(intent);
    }

    private void onRefresh() {
        observeNotNull(mainViewModel.getRecommendAttractionList(), recommendAttractionList -> {
            binding.main.setRefreshing(false);
            recommendAttractionListAdapter.setRecords(recommendAttractionList);
        });
    }

    private void autoRefresh() {
        binding.main.setRefreshing(true);
        onRefresh();
    }

    private void onSearchBarClick(View target) {
        startActivity(new Intent(requireContext(), SearchActivity.class));
    }

    private void useLightStatusBar() {
        Task.call(() -> {
            setUseLightStatusBar(true);
            return null;
        }, Task.UI_THREAD_EXECUTOR);
    }

    @Override
    protected MainBinding createViewBinding(LayoutInflater inflater, ViewGroup container) {
        return MainBinding.inflate(inflater, container, false);
    }
}
