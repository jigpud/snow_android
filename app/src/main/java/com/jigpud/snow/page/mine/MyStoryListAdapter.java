package com.jigpud.snow.page.mine;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.jigpud.snow.R;
import com.jigpud.snow.bean.StoryListResponse;
import com.jigpud.snow.databinding.ItemStoryBinding;
import com.jigpud.snow.page.base.BaseAdapter;
import com.jigpud.snow.page.base.BaseViewHolder;
import com.jigpud.snow.util.format.DateFormatter;
import com.jigpud.snow.util.format.IntegerFormatter;
import org.jetbrains.annotations.NotNull;

/**
 * @author jigpud
 */
public class MyStoryListAdapter extends BaseAdapter<StoryListResponse, MyStoryListAdapter.StoryListViewHolder> {
    @NonNull
    @NotNull
    @Override
    public StoryListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story, parent, false);
        return new StoryListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StoryListViewHolder holder, int position) {
        ItemStoryBinding itemStoryBinding = holder.binding;
        StoryListResponse story = getRecord(position);

        itemStoryBinding.nickname.setVisibility(View.GONE);
        itemStoryBinding.avatar.setVisibility(View.GONE);

        if (!story.getPictures().isEmpty()) {
            itemStoryBinding.picturesCount.setVisibility(View.VISIBLE);
            itemStoryBinding.picturesCount.setText(IntegerFormatter.toString(story.getPictures().size()));
        } else {
            itemStoryBinding.picturesCount.setVisibility(View.GONE);
        }

        itemStoryBinding.title.setText(story.getTitle());

        itemStoryBinding.releaseTime.setText(DateFormatter.yearMonthDayHourMinute(story.getReleaseTime()));

        itemStoryBinding.content.setText(story.getContent());

        itemStoryBinding.releaseLocation.setText(story.getReleaseLocation());

        Context context = itemStoryBinding.getRoot().getContext();
        int likesColor = ContextCompat.getColor(context, R.color.text_dark_mid);
        if (story.getLiked()) {
            likesColor = ContextCompat.getColor(context, R.color.primary);
        }
        itemStoryBinding.likes.setIconTint(ColorStateList.valueOf(likesColor));
        itemStoryBinding.likesCount.setText(IntegerFormatter.formatWithUnit(story.getLikes()));
    }

    @Override
    protected boolean areItemsTheSame(StoryListResponse oldRecord, StoryListResponse newRecord) {
        return oldRecord.getStoryId().equals(newRecord.getStoryId());
    }

    @Override
    protected boolean areContentsTheSame(StoryListResponse oldRecord, StoryListResponse newRecord) {
        return oldRecord.equals(newRecord);
    }

    public static class StoryListViewHolder extends BaseViewHolder<ItemStoryBinding> {
        public StoryListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        @Override
        protected ItemStoryBinding createViewBinding(View itemView) {
            return ItemStoryBinding.bind(itemView);
        }
    }
}
