package com.jigpud.snow.page.mine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.jigpud.snow.R;
import com.jigpud.snow.base.BaseViewHolder;
import com.jigpud.snow.database.entity.StoryEntity;
import com.jigpud.snow.databinding.ItemStoryBinding;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jigpud
 */
public class StoryListAdapter extends RecyclerView.Adapter<StoryListAdapter.MyStoryListViewHolder> {
    private final List<StoryEntity> stories = new ArrayList<>();

    @NonNull
    @NotNull
    @Override
    public MyStoryListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story, parent, false);
        return new MyStoryListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyStoryListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public void setStories(List<StoryEntity> stories) {
        MyStoryListDiffCallback diffCallback = new MyStoryListDiffCallback(this.stories, stories);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.stories.clear();
        this.stories.addAll(stories);
        diffResult.dispatchUpdatesTo(this);
    }

    public void addStories(List<StoryEntity> stories) {
        List<StoryEntity> newStories = new ArrayList<>();
        newStories.addAll(this.stories);
        newStories.addAll(stories);
        MyStoryListDiffCallback diffCallback = new MyStoryListDiffCallback(this.stories, newStories);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.stories.addAll(stories);
        diffResult.dispatchUpdatesTo(this);
    }

    public static class MyStoryListViewHolder extends BaseViewHolder<ItemStoryBinding> {
        public MyStoryListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        @Override
        protected ItemStoryBinding createViewBinding(View itemView) {
            return ItemStoryBinding.bind(itemView);
        }
    }

    private static class MyStoryListDiffCallback extends DiffUtil.Callback {
        private final List<StoryEntity> oldStories;
        private final List<StoryEntity> newStories;

        public MyStoryListDiffCallback(List<StoryEntity> oldStories, List<StoryEntity> newStories) {
            this.oldStories = oldStories;
            this.newStories = newStories;
        }

        @Override
        public int getOldListSize() {
            return oldStories.size();
        }

        @Override
        public int getNewListSize() {
            return newStories.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            StoryEntity oldStory = oldStories.get(oldItemPosition);
            StoryEntity newStory = newStories.get(newItemPosition);
            return oldStory.getStoryId().equals(newStory.getStoryId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            StoryEntity oldStory = oldStories.get(oldItemPosition);
            StoryEntity newStory = newStories.get(newItemPosition);
            return oldStory.equals(newStory);
        }
    }
}
