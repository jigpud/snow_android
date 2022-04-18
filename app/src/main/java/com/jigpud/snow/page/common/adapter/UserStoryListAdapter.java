package com.jigpud.snow.page.common.adapter;

import android.view.View;
import androidx.annotation.NonNull;
import com.jigpud.snow.R;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.database.entity.StoryEntity;
import com.jigpud.snow.databinding.ItemNoMoreFooterBinding;
import com.jigpud.snow.databinding.ItemStoryBinding;
import com.jigpud.snow.util.user.CurrentUser;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class UserStoryListAdapter extends StoryListAdapter {
    public UserStoryListAdapter(long pageSize, StoryClickListener clickListener) {
        super(pageSize, clickListener);
    }

    @Override
    protected void onBindRecordViewHolder(@NonNull @NotNull StoryListViewHolder holder, int position) {
        super.onBindRecordViewHolder(holder, position);
        StoryEntity story = getRecord(position);
        ItemStoryBinding binding = holder.binding;
        String currentUserid = CurrentUser.getInstance(SnowApplication.getAppContext()).getCurrentUserid();
        if (currentUserid != null && currentUserid.equals(story.getAuthorId())) {
            binding.avatar.setVisibility(View.GONE);
            binding.nickname.setVisibility(View.GONE);
        } else {
            binding.avatar.setVisibility(View.VISIBLE);
            binding.nickname.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onBindNoMoreFooterViewHolder(@NonNull @NotNull NoMoreFooterViewHolder holder, int position) {
        super.onBindNoMoreFooterViewHolder(holder, position);
        ItemNoMoreFooterBinding binding = holder.binding;
        if (!haveRecords()) {
            binding.footerText.setText(R.string.user_story_list_no_story);
        } else {
            binding.footerText.setText(R.string.hint_no_more_story);
        }
    }
}
