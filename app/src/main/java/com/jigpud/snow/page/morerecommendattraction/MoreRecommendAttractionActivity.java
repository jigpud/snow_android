package com.jigpud.snow.page.morerecommendattraction;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.jigpud.snow.databinding.MoreRecommendAttractionBinding;
import com.jigpud.snow.page.attractiondetail.AttractionDetailActivity;
import com.jigpud.snow.page.base.BaseActivity;
import com.jigpud.snow.page.common.itemdecoration.GridSpacingItemDecoration;
import com.jigpud.snow.util.constant.KeyConstant;

/**
 * @author : jigpud
 */
public class MoreRecommendAttractionActivity extends BaseActivity<MoreRecommendAttractionBinding> {
    private MoreRecommendAttractionViewModel moreRecommendAttractionViewModel;
    private MoreRecommendAttractionListAdapter moreRecommendAttractionListAdapter;

    @Override
    protected void preSetContent(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.preSetContent(savedInstanceState);

        moreRecommendAttractionViewModel = getViewModel(
                MoreRecommendAttractionViewModel.class, MoreRecommendAttractionViewModelFactory.create());

        moreRecommendAttractionListAdapter = new MoreRecommendAttractionListAdapter(
                MoreRecommendAttractionViewModel.RECOMMEND_ATTRACTION_LIST_PAGE_SIZE, this::onAttractionClick);
    }

    @Override
    protected void initView() {
        super.initView();

        binding.back.setOnClickListener(target -> finish());

        binding.recommendAttractionList.setOnRefreshListener(this::onRefresh);
        binding.recommendAttractionList.setOnLoadMoreListener(this::onLoadMore);
        binding.recommendAttractionList.setLoadMoreEnabled(false);
        binding.swipeTarget.setAdapter(moreRecommendAttractionListAdapter);
        binding.swipeTarget.setLayoutManager(new LinearLayoutManager(this));
        binding.swipeTarget.addItemDecoration(new GridSpacingItemDecoration(12));

        autoRefresh();
    }

    private void onRefresh() {
        observeNotNull(moreRecommendAttractionViewModel.refreshRecommendAttractionList(), recommendAttractionList -> {
            binding.recommendAttractionList.setRefreshing(false);
            moreRecommendAttractionListAdapter.setRecords(recommendAttractionList);
            binding.swipeTarget.scrollToPosition(0);
            binding.recommendAttractionList.setLoadMoreEnabled(
                    recommendAttractionList.size() >= MoreRecommendAttractionViewModel.RECOMMEND_ATTRACTION_LIST_PAGE_SIZE);
        });
    }

    private void autoRefresh() {
        binding.recommendAttractionList.setRefreshing(true);
    }

    private void onLoadMore() {
        observeNotNull(moreRecommendAttractionViewModel.loadMoreRecommendAttractionList(), recommendAttractionList -> {
            binding.recommendAttractionList.setLoadingMore(false);
            moreRecommendAttractionListAdapter.addRecords(recommendAttractionList);
            binding.recommendAttractionList.setLoadMoreEnabled(
                    recommendAttractionList.size() >= MoreRecommendAttractionViewModel.RECOMMEND_ATTRACTION_LIST_PAGE_SIZE);
        });
    }

    private void onAttractionClick(String attractionId) {
        Intent intent = new Intent(this, AttractionDetailActivity.class);
        intent.putExtra(KeyConstant.KEY_ATTRACTION_ID, attractionId);
        startActivity(intent);
    }

    @Override
    protected MoreRecommendAttractionBinding createViewBinding() {
        return MoreRecommendAttractionBinding.inflate(getLayoutInflater());
    }
}
