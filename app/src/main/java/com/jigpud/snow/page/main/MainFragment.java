package com.jigpud.snow.page.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import bolts.Task;
import com.jigpud.snow.databinding.MainBinding;
import com.jigpud.snow.page.base.BaseFragment;
import com.jigpud.snow.page.search.SearchActivity;

/**
 * @author jigpud
 */
public class MainFragment extends BaseFragment<MainBinding> {
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        super.initView();

        binding.searchBar.search.setKeyListener(null);
        binding.searchBar.search.setFocusable(false);
        binding.searchBar.search.setOnClickListener(this::onSearchBarClick);
        binding.searchBar.getRoot().setOnClickListener(this::onSearchBarClick);
    }

    @Override
    public void onResume() {
        super.onResume();
        useLightStatusBar();
    }

    private void onSearchBarClick(View target) {
        startActivity(new Intent(getContext(), SearchActivity.class));
    }

    private void useLightStatusBar() {
        Task.call(() -> {
            setUseLightStatusBar(true);
            return null;
        }, Task.UI_THREAD_EXECUTOR);
    }

    @Override
    protected MainBinding createViewBinding(LayoutInflater inflater, ViewGroup container) {
        return MainBinding.inflate(inflater, container, false);
    }
}
