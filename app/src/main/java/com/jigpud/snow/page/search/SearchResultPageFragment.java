package com.jigpud.snow.page.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.jigpud.snow.databinding.SearchResultPageBinding;
import com.jigpud.snow.page.base.BaseFragment;

/**
 * @author : jigpud
 */
public abstract class SearchResultPageFragment extends BaseFragment<SearchResultPageBinding> implements OnRefreshListener, OnLoadMoreListener {
    protected final String keyWords;

    public SearchResultPageFragment(String keyWords) {
        this.keyWords = keyWords;
    }

    @Override
    protected void initView() {
        super.initView();
        binding.searchResult.setOnRefreshListener(this);
        binding.searchResult.setOnLoadMoreListener(this);
    }

    @Override
    protected SearchResultPageBinding createViewBinding(LayoutInflater inflater, ViewGroup container) {
        return SearchResultPageBinding.inflate(inflater, container, false);
    }
}
