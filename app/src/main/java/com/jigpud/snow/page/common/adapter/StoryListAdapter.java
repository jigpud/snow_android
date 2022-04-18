package com.jigpud.snow.page.common.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.jigpud.snow.R;
import com.jigpud.snow.database.entity.StoryEntity;
import com.jigpud.snow.databinding.ItemNoMoreFooterBinding;
import com.jigpud.snow.databinding.ItemStoryBinding;
import com.jigpud.snow.page.base.BaseViewHolder;
import com.jigpud.snow.util.format.DateFormatter;
import com.jigpud.snow.util.format.IntegerFormatter;
import com.jigpud.snow.util.img.ImageLoader;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class StoryListAdapter extends NoMoreFooterAdapter<StoryEntity, StoryListAdapter.StoryListViewHolder> {
    private final StoryClickListener clickListener;

    public StoryListAdapter(long pageSize, StoryClickListener clickListener) {
        super(pageSize);
        this.clickListener = clickListener;
    }

    @Override
    protected StoryListViewHolder onCreateRecordViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story, parent, false);
        return new StoryListViewHolder(view);
    }

    @Override
    protected void onBindRecordViewHolder(@NonNull @NotNull StoryListViewHolder holder, int position) {
        StoryEntity story = getRecord(position);
        ItemStoryBinding binding = holder.binding;

        binding.getRoot().setOnClickListener(target -> clickListener.onStoryClick(story.getStoryId()));

        if (story.getPictures().size() < 2) {
            binding.picturesCount.setVisibility(View.GONE);
        } else {
            binding.picturesCount.setVisibility(View.VISIBLE);
            binding.picturesCount.setText(IntegerFormatter.toString(story.getPictures().size()));
        }
        String storyCover = "";
        if (!story.getPictures().isEmpty()) {
            storyCover = story.getPictures().get(0);
        }
        ImageLoader.loadImgFromUrl(
                binding.cover,
                storyCover,
                R.drawable.ic_placeholder_story_cover,
                R.drawable.ic_placeholder_story_cover
        );

        binding.avatar.setOnClickListener(target -> clickListener.onAuthorClick(story.getAuthorId()));
        ImageLoader.loadImgFromUrl(
                binding.avatar,
                story.getAuthorAvatar(),
                R.drawable.ic_placeholder_avatar,
                R.drawable.ic_placeholder_avatar
        );
        binding.nickname.setText(story.getAuthorNickname());
        binding.nickname.setOnClickListener(target -> clickListener.onAuthorClick(story.getAuthorId()));

        binding.title.setText(story.getTitle());

        binding.releaseTime.setText(DateFormatter.yearMonthDayHourMinute(story.getReleaseTime()));

        binding.content.setText(story.getContent());

        binding.releaseLocation.setText(story.getReleaseLocation());

        Context context = binding.getRoot().getContext();
        int likesColor = ContextCompat.getColor(context, R.color.text_dark_mid);
        if (story.isLiked()) {
            likesColor = ContextCompat.getColor(context, R.color.primary);
        }
        binding.likes.setIconTint(ColorStateList.valueOf(likesColor));
        binding.likes.setOnClickListener(target -> {
            if (story.isLiked()) {
                clickListener.onUnlike(story.getStoryId());
            } else {
                clickListener.onLike(story.getStoryId());
            }
        });
        binding.likesCount.setText(IntegerFormatter.formatWithUnit(story.getLikes()));
    }

    @Override
    protected void onBindNoMoreFooterViewHolder(@NonNull @NotNull NoMoreFooterViewHolder holder, int position) {
        super.onBindNoMoreFooterViewHolder(holder, position);
        ItemNoMoreFooterBinding binding = holder.binding;
        binding.footerText.setText(R.string.hint_no_more_story);
    }

    @Override
    protected boolean areItemsTheSame(StoryEntity oldRecord, StoryEntity newRecord) {
        return oldRecord.getStoryId().equals(newRecord.getStoryId());
    }

    @Override
    protected boolean areContentsTheSame(StoryEntity oldRecord, StoryEntity newRecord) {
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

    public interface StoryClickListener {
        void onStoryClick(String storyId);

        void onLike(String storyId);

        void onUnlike(String storyId);

        void onAuthorClick(String authorId);
    }
}
