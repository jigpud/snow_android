package com.jigpud.snow.page.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.SearchResultBinding;
import com.jigpud.snow.page.base.BaseFragment;
import com.jigpud.snow.page.common.SimpleFragmentStateAdapter;

/**
 * @author : jigpud
 */
public class SearchResultFragment extends BaseFragment<SearchResultBinding> {
    private static final String TAG = "SearchResultFragment";
    private static final int[] tabs = new int[] {
            R.string.search_result_attraction,
            R.string.search_result_user,
            R.string.search_result_story
    };

    private final String keyWords;

    public SearchResultFragment(String keyWords) {
        this.keyWords = keyWords;
    }

    @Override
    protected void initView() {
        super.initView();
        Fragment[] fragments = new Fragment[] {
                new AttractionSearchResultFragment(keyWords),
                new UserSearchResultFragment(keyWords),
                new StorySearchResultFragment(keyWords)
        };
        binding.searchResultPage.setAdapter(new SimpleFragmentStateAdapter(fragments, this));
        new TabLayoutMediator(binding.searchResultTab, binding.searchResultPage,
                ((tab, position) -> tab.setText(tabs[position]))).attach();
    }

    @Override
    protected SearchResultBinding createViewBinding(LayoutInflater inflater, ViewGroup container) {
        return SearchResultBinding.inflate(inflater, container, false);
    }
}
