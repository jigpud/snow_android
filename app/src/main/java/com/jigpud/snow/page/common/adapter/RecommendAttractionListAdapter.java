package com.jigpud.snow.page.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.jigpud.snow.R;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.databinding.ItemRecommendAttractionBinding;
import com.jigpud.snow.page.base.BaseAdapter;
import com.jigpud.snow.page.base.BaseViewHolder;
import com.jigpud.snow.util.format.AttractionAbstractFormatter;
import com.jigpud.snow.util.img.ImageLoader;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class RecommendAttractionListAdapter extends
        BaseAdapter<AttractionEntity, RecommendAttractionListAdapter.RecommendAttractionListViewHolder> {
    private final RecommendAttractionClickListener clickListener;

    public RecommendAttractionListAdapter(RecommendAttractionClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @NotNull
    @Override
    public RecommendAttractionListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_attraction, parent, false);
        return new RecommendAttractionListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecommendAttractionListViewHolder holder, int position) {
        AttractionEntity attraction = getRecord(position);
        ItemRecommendAttractionBinding binding = holder.binding;

        holder.itemView.setOnClickListener(target -> clickListener.onAttractionClick(attraction.getAttractionId()));

        if (!attraction.getPhotos().isEmpty()) {
            ImageLoader.loadImgFromUrl(
                    binding.cover,
                    attraction.getPhotos().get(0),
                    R.drawable.ic_placeholder_attraction_cover,
                    R.drawable.ic_placeholder_attraction_cover
            );
        } else {
            binding.cover.setImageResource(R.drawable.ic_placeholder_attraction_cover);
        }

        binding.name.setText(attraction.getName());

        String attractionAbstract = AttractionAbstractFormatter.getRecommendAttractionAbstract(
                attraction.getScore(), attraction.getStoryCount());
        binding.attractionAbstract.setText(attractionAbstract);
    }

    @Override
    protected boolean areItemsTheSame(AttractionEntity oldRecord, AttractionEntity newRecord) {
        return oldRecord.getAttractionId().equals(newRecord.getAttractionId());
    }

    @Override
    protected boolean areContentsTheSame(AttractionEntity oldRecord, AttractionEntity newRecord) {
        return oldRecord.equals(newRecord);
    }

    public static class RecommendAttractionListViewHolder extends BaseViewHolder<ItemRecommendAttractionBinding> {
        public RecommendAttractionListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        @Override
        protected ItemRecommendAttractionBinding createViewBinding(View itemView) {
            return ItemRecommendAttractionBinding.bind(itemView);
        }
    }

    public interface RecommendAttractionClickListener {
        void onAttractionClick(String attractionId);
    }
}
