package com.jigpud.snow.page.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.SearchBinding;
import com.jigpud.snow.page.base.BaseActivity;
import com.jigpud.snow.util.logger.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author : jigpud
 */
public class SearchActivity extends BaseActivity<SearchBinding> {
    private static final String TAG = "SearchActivity";
    private SearchViewModel searchViewModel;
    private SearchMainFragment searchMainFragment;

    @Override
    protected void preSetContent(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.preSetContent(savedInstanceState);
        searchViewModel = getViewModel(SearchViewModel.class, SearchViewModelFactory.create());
        searchMainFragment = new SearchMainFragment();
    }

    @Override
    protected void initView() {
        super.initView();

        showSearchMain();

        binding.close.setOnClickListener(target -> finish());

        binding.searchBar.search.setOnEditorActionListener(this::onSearchClick);
        binding.searchBar.search.addTextChangedListener(onSearchKeyWordsChanged());
    }

    private TextWatcher onSearchKeyWordsChanged() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    showSearchMain();
                }
            }
        };
    }

    private boolean onSearchClick(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String keyWords = textView.getText().toString();
            if (!keyWords.isEmpty()) {
                EventBus.getDefault().post(new OnSearchEvent(keyWords));
            }
            return true;
        }
        return false;
    }

    private void showSearchResult(String keyWords) {
        replaceSearchContent(new SearchResultFragment(keyWords));
    }

    private void showSearchMain() {
        replaceSearchContent(searchMainFragment);
    }

    private void replaceSearchContent(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.search_content, fragment)
                .commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchEvent(OnSearchEvent event) {
        String keyWords = event.getKeyWords();
        Logger.d(TAG, "onSearch: keyWords -> %s", keyWords);
        if (keyWords != null && !keyWords.isEmpty()) {
            binding.searchBar.search.setText(keyWords);
            binding.searchBar.search.clearFocus();
            searchViewModel.addSearchHistory(keyWords);
            showSearchResult(keyWords);
        }
    }

    @Override
    protected boolean needEventBus() {
        return true;
    }

    @Override
    protected SearchBinding createViewBinding() {
        return SearchBinding.inflate(getLayoutInflater());
    }
}
