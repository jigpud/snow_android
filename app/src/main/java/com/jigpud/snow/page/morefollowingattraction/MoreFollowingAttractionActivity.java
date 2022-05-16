package com.jigpud.snow.page.morefollowingattraction;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.jigpud.snow.databinding.MoreFollowingAttractionBinding;
import com.jigpud.snow.page.attractiondetail.AttractionDetailActivity;
import com.jigpud.snow.page.base.BaseActivity;
import com.jigpud.snow.util.constant.KeyConstant;

/**
 * @author : jigpud
 */
public class MoreFollowingAttractionActivity extends BaseActivity<MoreFollowingAttractionBinding> {
    private MoreFollowingAttractionViewModel moreFollowingAttractionViewModel;
    private MoreFollowingAttractionListAdapter attractionListAdapter;

    @Override
    protected void preSetContent(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.preSetContent(savedInstanceState);

        moreFollowingAttractionViewModel = getViewModel(
                MoreFollowingAttractionViewModel.class, MoreFollowingAttractionViewModelFactory.create());

        attractionListAdapter = new MoreFollowingAttractionListAdapter(
                MoreFollowingAttractionViewModel.FOLLOWING_ATTRACTION_LIST_PAGE_SIZE, this::onAttractionClick);
    }

    @Override
    protected void initView() {
        super.initView();

        binding.back.setOnClickListener(target -> finish());

        binding.followingAttractionList.setOnLoadMoreListener(this::onLoadMore);
        binding.followingAttractionList.setOnRefreshListener(this::onRefresh);
        binding.followingAttractionList.setLoadMoreEnabled(false);
        binding.swipeTarget.setLayoutManager(new LinearLayoutManager(this));
        binding.swipeTarget.setAdapter(attractionListAdapter);

        autoRefresh();
    }

    private void onAttractionClick(String attractionId) {
        Intent intent = new Intent(this, AttractionDetailActivity.class);
        intent.putExtra(KeyConstant.KEY_ATTRACTION_ID, attractionId);
        startActivity(intent);
    }

    private void onRefresh() {
        observeNotNull(moreFollowingAttractionViewModel.refreshFollowingAttractionList(), followingAttractionList -> {
            binding.followingAttractionList.setRefreshing(false);
            attractionListAdapter.setRecords(followingAttractionList);
            binding.swipeTarget.scrollToPosition(0);
            binding.followingAttractionList.setLoadMoreEnabled(
                    followingAttractionList.size() >= MoreFollowingAttractionViewModel.FOLLOWING_ATTRACTION_LIST_PAGE_SIZE);
        });
    }

    private void autoRefresh() {
        binding.followingAttractionList.setRefreshing(true);
    }

    private void onLoadMore() {
        observeNotNull(moreFollowingAttractionViewModel.loadMoreFollowingAttractionList(), followingAttractionList -> {
            binding.followingAttractionList.setLoadMoreEnabled(false);
            attractionListAdapter.addRecords(followingAttractionList);
            binding.followingAttractionList.setLoadMoreEnabled(
                    followingAttractionList.size() >= MoreFollowingAttractionViewModel.FOLLOWING_ATTRACTION_LIST_PAGE_SIZE);
        });
    }

    @Override
    protected MoreFollowingAttractionBinding createViewBinding() {
        return MoreFollowingAttractionBinding.inflate(getLayoutInflater());
    }
}
