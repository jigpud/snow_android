package com.jigpud.snow.page.moments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.jigpud.snow.R;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.databinding.ItemFollowingAttractionBinding;
import com.jigpud.snow.page.base.BaseAdapter;
import com.jigpud.snow.page.base.BaseViewHolder;
import com.jigpud.snow.util.format.AttractionFormatter;
import com.jigpud.snow.util.img.ImageLoader;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class FollowingAttractionListAdapter extends
        BaseAdapter<AttractionEntity, FollowingAttractionListAdapter.FollowingAttractionListViewHolder> {
    private final AttractionClickListener clickListener;

    public FollowingAttractionListAdapter(AttractionClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @NotNull
    @Override
    public FollowingAttractionListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_following_attraction, parent, false);
        return new FollowingAttractionListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FollowingAttractionListViewHolder holder, int position) {
        AttractionEntity attraction = getRecord(position);
        ItemFollowingAttractionBinding binding = holder.binding;

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

        binding.followersCount.setText(AttractionFormatter.formatFollowerCount(attraction.getFollowers()));

        binding.storyCount.setText(AttractionFormatter.formatStoryCountBefore(attraction.getStoryCount()));
    }

    public static class FollowingAttractionListViewHolder extends BaseViewHolder<ItemFollowingAttractionBinding> {
        public FollowingAttractionListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        @Override
        protected ItemFollowingAttractionBinding createViewBinding(View itemView) {
            return ItemFollowingAttractionBinding.bind(itemView);
        }
    }

    public interface AttractionClickListener {
        void onAttractionClick(String attractionId);
    }
}
