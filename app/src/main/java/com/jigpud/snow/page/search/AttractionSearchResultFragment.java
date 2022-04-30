package com.jigpud.snow.page.search;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.jigpud.snow.page.attractiondetail.AttractionDetailActivity;
import com.jigpud.snow.page.common.itemdecoration.GridSpacingItemDecoration;
import com.jigpud.snow.util.constant.KeyConstant;

/**
 * @author : jigpud
 */
public class AttractionSearchResultFragment extends SearchResultPageFragment {
    private SearchViewModel searchViewModel;
    private AttractionSearchResultAdapter attractionSearchResultAdapter;

    public AttractionSearchResultFragment(String keyWords) {
        super(keyWords);
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchViewModel = getActivityViewModel(SearchViewModel.class, SearchViewModelFactory.create());

        attractionSearchResultAdapter = new AttractionSearchResultAdapter(SearchViewModel.SEARCH_RESULT_PAGE_SIZE, this::onAttractionClick);
    }

    @Override
    protected void initView() {
        super.initView();

        binding.searchResult.setLoadMoreEnabled(false);

        binding.swipeTarget.setAdapter(attractionSearchResultAdapter);
        binding.swipeTarget.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.swipeTarget.setItemAnimator(null);
        binding.swipeTarget.addItemDecoration(new GridSpacingItemDecoration(12));

        autoRefresh();
    }

    @Override
    public void onRefresh() {
        observeNotNull(searchViewModel.searchAttraction(keyWords), attractionSearchResult -> {
            binding.searchResult.setRefreshing(false);
            attractionSearchResultAdapter.setRecords(attractionSearchResult);
            binding.swipeTarget.scrollToPosition(0);
            binding.searchResult.setLoadMoreEnabled(attractionSearchResult.size() >= SearchViewModel.SEARCH_RESULT_PAGE_SIZE);
        });
    }

    @Override
    public void onLoadMore() {
        observeNotNull(searchViewModel.loadMoreAttractionSearchResult(keyWords), attractionSearchResult -> {
            binding.searchResult.setLoadingMore(false);
            attractionSearchResultAdapter.addRecords(attractionSearchResult);
            binding.searchResult.setLoadMoreEnabled(attractionSearchResult.size() >= SearchViewModel.SEARCH_RESULT_PAGE_SIZE);
        });
    }

    private void onAttractionClick(String attractionId) {
        Intent intent = new Intent(requireContext(), AttractionDetailActivity.class);
        intent.putExtra(KeyConstant.KEY_ATTRACTION_ID, attractionId);
        startActivity(intent);
    }
}
