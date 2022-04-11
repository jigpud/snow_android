package com.jigpud.snow.page.search;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.jigpud.snow.page.common.adapter.StoryListAdapter;
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
        binding.swipeTarget.setLayoutManager(new LinearLayoutManager(getContext()));

        onRefresh();
    }

    @Override
    public void onLoadMore() {
        binding.searchResult.setLoadingMore(true);
        observeNotNull(searchViewModel.moreStorySearchResult(keyWords), storySearchResult -> {
            Logger.d(TAG, "onLoadMore: %s", storySearchResult);
            binding.searchResult.setLoadingMore(false);
            storySearchResultAdapter.addRecords(storySearchResult);
            binding.searchResult.setLoadMoreEnabled(storySearchResult.size() >= SearchViewModel.SEARCH_RESULT_PAGE_SIZE);
        });
    }

    @Override
    public void onRefresh() {
        binding.searchResult.setRefreshing(true);
        observeNotNull(searchViewModel.searchStory(keyWords), storySearchResult -> {
            Logger.d(TAG, "onRefresh: %s", storySearchResult);
            binding.searchResult.setRefreshing(false);
            storySearchResultAdapter.setRecords(storySearchResult);
            binding.searchResult.setLoadMoreEnabled(storySearchResult.size() >= SearchViewModel.SEARCH_RESULT_PAGE_SIZE);
        });
    }

    @Override
    public void onStoryClick(String storyId) {

    }

    @Override
    public void onLike(String storyId) {

    }

    @Override
    public void onUnlike(String storyId) {

    }

    @Override
    public void onAuthorClick(String authorId) {

    }
}
