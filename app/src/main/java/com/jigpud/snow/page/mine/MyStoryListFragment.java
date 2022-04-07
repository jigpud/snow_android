package com.jigpud.snow.page.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jigpud.snow.databinding.MineStoryListBinding;
import com.jigpud.snow.page.base.BaseFragment;
import com.jigpud.snow.util.logger.Logger;

/**
 * @author jigpud
 */
public class MyStoryListFragment extends BaseFragment<MineStoryListBinding> {
    private static final String TAG = "MyStoryListFragment";

    private MineViewModel mineViewModel;
    private final MyStoryListAdapter myStoryListAdapter = new MyStoryListAdapter();

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mineViewModel = getParentViewModel(MineViewModel.class);
    }

    @Override
    protected void initView() {
        super.initView();
        binding.swipeToLoad.setOnRefreshListener(this::onRefresh);
        binding.swipeToLoad.setOnLoadMoreListener(this::onLoadMore);
        binding.swipeTarget.setAdapter(myStoryListAdapter);
        binding.swipeTarget.setLayoutManager(new LinearLayoutManager(getContext()));

        onRefresh();
    }

    private void onRefresh() {
        SwipeToLoadLayout swipeToLoadLayout = binding.swipeToLoad;
        swipeToLoadLayout.setRefreshing(true);
        observeNotNull(mineViewModel.refreshMyStoryList(), storyList -> {
            Logger.d(TAG, "refresh: %s", storyList.toString());
            swipeToLoadLayout.setRefreshing(false);
            myStoryListAdapter.setRecords(storyList);
        });
    }

    private void onLoadMore() {
        SwipeToLoadLayout swipeToLoadLayout = binding.swipeToLoad;
        swipeToLoadLayout.setLoadingMore(true);
        observeNotNull(mineViewModel.loadMoreStoryList(), storyList -> {
            Logger.d(TAG, "load more: %s", storyList.toString());
            swipeToLoadLayout.setLoadingMore(false);
            myStoryListAdapter.addRecords(storyList);
        });
    }

    @Override
    protected MineStoryListBinding createViewBinding(LayoutInflater inflater, ViewGroup container) {
        return MineStoryListBinding.inflate(inflater, container, false);
    }
}
