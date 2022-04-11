package com.jigpud.snow.page.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.SearchMainBinding;
import com.jigpud.snow.page.base.BaseFragment;
import org.greenrobot.eventbus.EventBus;

/**
 * @author : jigpud
 */
public class SearchMainFragment extends BaseFragment<SearchMainBinding> implements SearchHistoryAdapter.SearchHistoryClickListener {
    private SearchViewModel searchViewModel;
    private SearchHistoryAdapter searchHistoryAdapter;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchViewModel = getActivityViewModel(SearchViewModel.class, SearchViewModelFactory.create());
        searchHistoryAdapter = new SearchHistoryAdapter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        binding.clearHistory.setOnClickListener(this::onClearHistoryClick);
        binding.searchHistoryList.setAdapter(searchHistoryAdapter);
        binding.searchHistoryList.setLayoutManager(new FlexboxLayoutManager(requireContext()));
        binding.searchHistoryList.addItemDecoration(searchHistoryItemDecoration());
        observeNotNull(searchViewModel.getSearchHistory(), searchHistory -> {
            if (searchHistory.isEmpty()) {
                binding.noSearchHistoryHint.setVisibility(View.VISIBLE);
                searchHistoryAdapter.setRecords(searchHistory);
            } else {
                searchHistoryAdapter.setRecords(searchHistory);
                binding.noSearchHistoryHint.setVisibility(View.GONE);
            }
        });
    }

    private FlexboxItemDecoration searchHistoryItemDecoration() {
        FlexboxItemDecoration flexboxItemDecoration = new FlexboxItemDecoration(requireContext());
        flexboxItemDecoration.setDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_search_history_item_divier));
        return flexboxItemDecoration;
    }

    private void onClearHistoryClick(View target) {
        searchViewModel.clearSearchHistory();
    }

    @Override
    protected SearchMainBinding createViewBinding(LayoutInflater inflater, ViewGroup container) {
        return SearchMainBinding.inflate(inflater, container, false);
    }

    @Override
    public void onSearchHistoryClick(String history) {
        EventBus.getDefault().post(new OnSearchEvent(history));
    }

    @Override
    public void onDeleteSearchHistoryClick(String history) {
        searchViewModel.deleteSearchHistory(history);
    }
}
