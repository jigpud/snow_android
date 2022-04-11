package com.jigpud.snow.page.search;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.jigpud.snow.page.common.UserListAdapter;

/**
 * @author : jigpud
 */
public class UserSearchResultFragment extends SearchResultPageFragment implements UserListAdapter.UserClickListener {
    private UserSearchResultAdapter userSearchResultAdapter;
    private SearchViewModel searchViewModel;

    public UserSearchResultFragment(String keyWords) {
        super(keyWords);
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchViewModel = getActivityViewModel(SearchViewModel.class, SearchViewModelFactory.create());
        userSearchResultAdapter = new UserSearchResultAdapter(SearchViewModel.SEARCH_RESULT_PAGE_SIZE, this);
    }

    @Override
    protected void initView() {
        super.initView();

        binding.swipeTarget.setAdapter(userSearchResultAdapter);
        binding.swipeTarget.setLayoutManager(new LinearLayoutManager(requireContext()));

        onRefresh();
    }

    @Override
    public void onLoadMore() {
        binding.searchResult.setLoadingMore(true);
        observeNotNull(searchViewModel.moreUserSearchResult(keyWords), userSearchResult -> {
            binding.searchResult.setLoadingMore(false);
            userSearchResultAdapter.addRecords(userSearchResult);
            binding.searchResult.setLoadMoreEnabled(userSearchResult.size() >= SearchViewModel.SEARCH_RESULT_PAGE_SIZE);
        });
    }

    @Override
    public void onRefresh() {
        binding.searchResult.setRefreshing(true);
        observeNotNull(searchViewModel.searchUser(keyWords), userSearchResult -> {
            binding.searchResult.setRefreshing(false);
            userSearchResultAdapter.setRecords(userSearchResult);
            binding.searchResult.setLoadMoreEnabled(userSearchResult.size() >= SearchViewModel.SEARCH_RESULT_PAGE_SIZE);
        });
    }

    @Override
    public void onUserClick(String userid) {

    }

    @Override
    public void onFollow(String userid) {

    }

    @Override
    public void onUnfollow(String userid) {

    }
}
