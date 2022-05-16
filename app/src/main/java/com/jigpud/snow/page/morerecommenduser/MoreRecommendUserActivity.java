package com.jigpud.snow.page.morerecommenduser;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.jigpud.snow.databinding.MoreRecommendUserBinding;
import com.jigpud.snow.page.base.BaseActivity;
import com.jigpud.snow.page.common.adapter.UserListAdapter;
import com.jigpud.snow.page.userprofile.UserProfileActivity;
import com.jigpud.snow.util.constant.KeyConstant;

/**
 * @author : jigpud
 */
public class MoreRecommendUserActivity extends BaseActivity<MoreRecommendUserBinding> implements UserListAdapter.UserClickListener {
    private MoreRecommendUserViewModel moreRecommendUserViewModel;
    private MoreRecommendUserListAdapter moreRecommendUserListAdapter;

    @Override
    protected void preSetContent(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.preSetContent(savedInstanceState);

        setUseLightStatusBar(true);

        moreRecommendUserViewModel = getViewModel(MoreRecommendUserViewModel.class, MoreRecommendUserViewModelFactory.create());

        moreRecommendUserListAdapter = new MoreRecommendUserListAdapter(MoreRecommendUserViewModel.RECOMMEND_USER_LIST_PAGE_SIZE, this);
    }

    @Override
    protected void initView() {
        super.initView();

        binding.back.setOnClickListener(target -> finish());

        binding.recommendUserList.setOnLoadMoreListener(this::onLoadMore);
        binding.recommendUserList.setOnRefreshListener(this::onRefresh);
        binding.recommendUserList.setLoadMoreEnabled(false);
        binding.swipeTarget.setLayoutManager(new LinearLayoutManager(this));
        binding.swipeTarget.setItemAnimator(null);
        binding.swipeTarget.setAdapter(moreRecommendUserListAdapter);

        autoRefresh();
    }

    @Override
    public void onUserClick(String userid) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra(KeyConstant.KEY_USERID, userid);
        startActivity(intent);
    }

    @Override
    public void onFollow(String userid) {
        observeNotNull(moreRecommendUserViewModel.follow(userid), followStatus -> {
            if (followStatus.first) {
                observeNotNull(moreRecommendUserViewModel.getUser(userid), moreRecommendUserListAdapter::updateRecord);
            }
        });
    }

    @Override
    public void onUnfollow(String userid) {
        observeNotNull(moreRecommendUserViewModel.unfollow(userid), unfollowStatus -> {
            if (unfollowStatus.first) {
                observeNotNull(moreRecommendUserViewModel.getUser(userid), moreRecommendUserListAdapter::updateRecord);
            }
        });
    }

    private void onRefresh() {
        observeNotNull(moreRecommendUserViewModel.refreshRecommendUserList(), recommendUserList -> {
            binding.recommendUserList.setRefreshing(false);
            moreRecommendUserListAdapter.setRecords(recommendUserList);
            binding.swipeTarget.scrollToPosition(0);
            binding.recommendUserList.setLoadMoreEnabled(recommendUserList.size() >= MoreRecommendUserViewModel.RECOMMEND_USER_LIST_PAGE_SIZE);
        });
    }

    private void autoRefresh() {
        binding.recommendUserList.setRefreshing(true);
    }

    private void onLoadMore() {
        observeNotNull(moreRecommendUserViewModel.loadMoreRecommendUserList(), recommendUserList -> {
            binding.recommendUserList.setLoadingMore(false);
            moreRecommendUserListAdapter.addRecords(recommendUserList);
            binding.recommendUserList.setLoadMoreEnabled(recommendUserList.size() >= MoreRecommendUserViewModel.RECOMMEND_USER_LIST_PAGE_SIZE);
        });
    }

    @Override
    protected MoreRecommendUserBinding createViewBinding() {
        return MoreRecommendUserBinding.inflate(getLayoutInflater());
    }
}
