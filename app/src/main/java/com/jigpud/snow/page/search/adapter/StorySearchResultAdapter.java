package com.jigpud.snow.page.search.adapter;

import androidx.annotation.NonNull;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.ItemNoMoreFooterBinding;
import com.jigpud.snow.page.common.StoryListAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class StorySearchResultAdapter extends StoryListAdapter {
    public StorySearchResultAdapter(long pageSize, StoryClickListener clickListener) {
        super(pageSize, clickListener);
    }

    @Override
    protected void onBindNoMoreFooterViewHolder(@NonNull @NotNull NoMoreFooterViewHolder holder, int position) {
        super.onBindNoMoreFooterViewHolder(holder, position);
        ItemNoMoreFooterBinding binding = holder.binding;
        if (haveRecords()) {
            binding.footerText.setText(R.string.hint_no_more_story);
        } else {
            binding.footerText.setText(R.string.hint_no_search_result);
        }
    }
}
