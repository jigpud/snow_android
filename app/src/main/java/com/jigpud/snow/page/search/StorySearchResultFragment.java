package com.jigpud.snow.page.search;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.jigpud.snow.page.common.adapter.StoryListAdapter;
import com.jigpud.snow.page.storydetail.StoryDetailActivity;
import com.jigpud.snow.util.constant.KeyConstant;
import com.jigpud.snow.util.logger.Logger;

/**
 * @author : jigpud
 */
public class StorySearchResultFragment extends SearchResultPageFragment implements StoryListAdapter.StoryClickListener {
    private static final String TAG = "StorySearchResultFragment";

    private StorySearchResultAdapter storySearchResultAdapter;
    private SearchViewModel searchViewModel;

    public StorySearchResultFragment(String keyWords) {
        super(keyWords);
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchViewModel = getActivityViewModel(SearchViewModel.class, SearchViewModelFactory.create());
        storySearchResultAdapter = new StorySearchResultAdapter(SearchViewModel.SEARCH_RESULT_PAGE_SIZE, this);
    }

    @Override
    protected void initView() {
        super.initView();

        binding.swipeTarget.setAdapter(storySearchResultAdapter);
        binding.swipeTarget.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.swipeTarget.setItemAnimator(null);

        autoRefresh();
    }

    @Override
    public void onLoadMore() {
        observeNotNull(searchViewModel.loadMoreStorySearchResult(keyWords), storySearchResult -> {
            Logger.d(TAG, "onLoadMore: %s", storySearchResult);
            binding.searchResult.setLoadingMore(false);
            storySearchResultAdapter.addRecords(storySearchResult);
            binding.searchResult.setLoadMoreEnabled(storySearchResult.size() >= SearchViewModel.SEARCH_RESULT_PAGE_SIZE);
        });
    }

    @Override
    public void onRefresh() {
        observeNotNull(searchViewModel.searchStory(keyWords), storySearchResult -> {
            Logger.d(TAG, "onRefresh: %s", storySearchResult);
            binding.searchResult.setRefreshing(false);
            storySearchResultAdapter.setRecords(storySearchResult);
            binding.swipeTarget.scrollToPosition(0);
            binding.searchResult.setLoadMoreEnabled(storySearchResult.size() >= SearchViewModel.SEARCH_RESULT_PAGE_SIZE);
        });
    }

    @Override
    public void onStoryClick(String storyId) {
        Intent intent = new Intent(requireContext(), StoryDetailActivity.class);
        intent.putExtra(KeyConstant.KEY_STORY_ID, storyId);
        startActivity(intent);
    }

    @Override
    public void onLikeStory(String storyId) {
        observeNotNull(searchViewModel.likeStory(storyId), likeStoryStatus -> {
            if (likeStoryStatus.first) {
                observeNotNull(searchViewModel.getStory(storyId), storySearchResultAdapter::updateRecord);
            }
        });
    }

    @Override
    public void onUnlikeStory(String storyId) {
        observeNotNull(searchViewModel.unlikeStory(storyId), unlikeStoryStatus -> {
            if (unlikeStoryStatus.first) {
                observeNotNull(searchViewModel.getStory(storyId), storySearchResultAdapter::updateRecord);
            }
        });
    }

    @Override
    public void onAuthorClick(String authorId) {

    }
}
