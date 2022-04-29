package com.jigpud.snow.page.attractiondetail;

import androidx.annotation.NonNull;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.ItemNoMoreFooterBinding;
import com.jigpud.snow.page.common.adapter.StoryListAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class AttractionStoryListAdapter extends StoryListAdapter {
    public AttractionStoryListAdapter(long pageSize, StoryClickListener clickListener) {
        super(pageSize, clickListener);
    }

    @Override
    protected void onBindNoMoreFooterViewHolder(@NonNull @NotNull NoMoreFooterViewHolder holder, int position) {
        super.onBindNoMoreFooterViewHolder(holder, position);
        ItemNoMoreFooterBinding binding = holder.binding;
        if (getRecords().isEmpty()) {
            binding.footerText.setText(R.string.hint_no_attraction_story);
        } else {
            binding.footerText.setText(R.string.hint_no_more_story);
        }
    }
}
