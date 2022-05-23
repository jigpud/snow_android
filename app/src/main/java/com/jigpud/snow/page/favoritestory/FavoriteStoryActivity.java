package com.jigpud.snow.page.favoritestory;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.jigpud.snow.databinding.FavoriteBinding;
import com.jigpud.snow.page.base.BaseActivity;
import com.jigpud.snow.page.storydetail.StoryDetailActivity;
import com.jigpud.snow.page.userprofile.UserProfileActivity;
import com.jigpud.snow.util.constant.KeyConstant;

/**
 * @author : jigpud
 */
public class FavoriteStoryActivity extends BaseActivity<FavoriteBinding> implements MyFavoriteStoryListAdapter.StoryClickListener {
    private FavoriteStoryViewModel favoriteStoryViewModel;
    private MyFavoriteStoryListAdapter myFavoriteStoryListAdapter;

    @Override
    protected void preSetContent(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.preSetContent(savedInstanceState);

        favoriteStoryViewModel = getViewModel(FavoriteStoryViewModel.class, FavoriteStoryViewModelFactory.create());

        myFavoriteStoryListAdapter = new MyFavoriteStoryListAdapter(FavoriteStoryViewModel.FAVORITE_STORY_LIST_SIZE, this);
    }

    @Override
    protected void initView() {
        super.initView();

        binding.back.setOnClickListener(target -> finish());

        binding.favoriteList.setLoadMoreEnabled(false);
        binding.favoriteList.setOnRefreshListener(this::onRefresh);
        binding.favoriteList.setOnLoadMoreListener(this::onLoadMore);
        binding.swipeTarget.setAdapter(myFavoriteStoryListAdapter);
        binding.swipeTarget.setLayoutManager(new LinearLayoutManager(this));
        binding.swipeTarget.setItemAnimator(null);

        autoRefresh();
    }

    @Override
    public void onUnFavoriteStory(String storyId) {
        observeNotNull(favoriteStoryViewModel.unFavoriteStory(storyId), unFavoriteStoryStatus -> {
            if (unFavoriteStoryStatus.first) {
                observeNotNull(favoriteStoryViewModel.getStory(storyId), myFavoriteStoryListAdapter::deleteRecord);
            }
        });
    }

    @Override
    public void onLikeStory(String storyId) {
        observeNotNull(favoriteStoryViewModel.likeStory(storyId), likeStoryStatus -> {
            if (likeStoryStatus.first) {
                observeNotNull(favoriteStoryViewModel.getStory(storyId), myFavoriteStoryListAdapter::updateRecord);
            }
        });
    }

    @Override
    public void onUnlikeStory(String storyId) {
        observeNotNull(favoriteStoryViewModel.unlikeStory(storyId), unlikeStoryStatus -> {
            if (unlikeStoryStatus.first) {
                observeNotNull(favoriteStoryViewModel.getStory(storyId), myFavoriteStoryListAdapter::updateRecord);
            }
        });
    }

    @Override
    public void onStoryClick(String storyId) {
        Intent intent = new Intent(this, StoryDetailActivity.class);
        intent.putExtra(KeyConstant.KEY_STORY_ID, storyId);
        startActivity(intent);
    }

    @Override
    public void onAuthorClick(String author) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra(KeyConstant.KEY_USERID, author);
        startActivity(intent);
    }

    private void onRefresh() {
        observeNotNull(favoriteStoryViewModel.refreshMyFavoriteStoryList(), favoriteStoryList -> {
            binding.favoriteList.setRefreshing(false);
            myFavoriteStoryListAdapter.setRecords(favoriteStoryList);
            binding.swipeTarget.scrollToPosition(0);
            binding.favoriteList.setLoadMoreEnabled(favoriteStoryList.size() >= FavoriteStoryViewModel.FAVORITE_STORY_LIST_SIZE);
        });
    }

    private void autoRefresh() {
        binding.favoriteList.setRefreshing(true);
    }

    private void onLoadMore() {
        observeNotNull(favoriteStoryViewModel.loadMoreMyFavoriteStoryList(), favoriteStoryList -> {
            binding.favoriteList.setLoadingMore(false);
            myFavoriteStoryListAdapter.addRecords(favoriteStoryList);
            binding.favoriteList.setLoadMoreEnabled(favoriteStoryList.size() >= FavoriteStoryViewModel.FAVORITE_STORY_LIST_SIZE);
        });
    }

    @Override
    protected FavoriteBinding createViewBinding() {
        return FavoriteBinding.inflate(getLayoutInflater());
    }
}
