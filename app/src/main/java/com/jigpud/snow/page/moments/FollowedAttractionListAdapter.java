package com.jigpud.snow.page.moments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.jigpud.snow.R;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.databinding.ItemFollowedAttractionBinding;
import com.jigpud.snow.page.base.BaseAdapter;
import com.jigpud.snow.page.base.BaseViewHolder;
import com.jigpud.snow.util.format.AttractionFormatter;
import com.jigpud.snow.util.img.ImageLoader;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class FollowedAttractionListAdapter extends
        BaseAdapter<AttractionEntity, FollowedAttractionListAdapter.FollowedAttractionListViewHolder> {
    private final AttractionClickListener clickListener;

    public FollowedAttractionListAdapter(AttractionClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @NotNull
    @Override
    public FollowedAttractionListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_followed_attraction, parent, false);
        return new FollowedAttractionListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FollowedAttractionListViewHolder holder, int position) {
        AttractionEntity attraction = getRecord(position);
        ItemFollowedAttractionBinding binding = holder.binding;

        binding.getRoot().setOnClickListener(target -> clickListener.onAttractionClick(attraction.getAttractionId()));

        String cover = "";
        if (!attraction.getPhotos().isEmpty()) {
            cover = attraction.getPhotos().get(0);
        }
        ImageLoader.loadImgFromUrl(
                binding.cover,
                cover,
                R.drawable.ic_placeholder_attraction_cover,
                R.drawable.ic_placeholder_attraction_cover
        );

        binding.name.setText(attraction.getName());

        binding.followersCount.setText(AttractionFormatter.formatFollowersCount(attraction.getFollowers()));

        binding.storyCount.setText(AttractionFormatter.formatStoryCountBefore(attraction.getStoryCount()));
    }

    public static class FollowedAttractionListViewHolder extends BaseViewHolder<ItemFollowedAttractionBinding> {
        public FollowedAttractionListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        @Override
        protected ItemFollowedAttractionBinding createViewBinding(View itemView) {
            return ItemFollowedAttractionBinding.bind(itemView);
        }
    }

    public interface AttractionClickListener {
        void onAttractionClick(String attractionId);
    }
}
