package com.jigpud.snow.page.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.jigpud.snow.R;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.databinding.ItemAttractionBinding;
import com.jigpud.snow.databinding.ItemNoMoreFooterBinding;
import com.jigpud.snow.page.base.BaseViewHolder;
import com.jigpud.snow.util.format.AttractionFormatter;
import com.jigpud.snow.util.format.FloatFormatter;
import com.jigpud.snow.util.img.ImageLoader;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public class AttractionListAdapter extends NoMoreFooterAdapter<AttractionEntity, AttractionListAdapter.AttractionListViewHolder> {
    private final AttractionClickListener clickListener;

    public AttractionListAdapter(long pageSize, AttractionClickListener clickListener) {
        super(pageSize);
        this.clickListener = clickListener;
    }

    @Override
    protected AttractionListViewHolder onCreateRecordViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attraction, parent, false);
        return new AttractionListViewHolder(view);
    }

    @Override
    protected void onBindRecordViewHolder(@NonNull @NotNull AttractionListViewHolder holder, int position) {
        AttractionEntity attraction = getRecord(position);
        ItemAttractionBinding binding = holder.binding;
        Context context = binding.getRoot().getContext();

        binding.attraction.setOnClickListener(target -> clickListener.onAttractionClick(attraction.getAttractionId()));

        String cover = "";
        if (attraction.getPictures() != null && !attraction.getPictures().isEmpty()) {
            cover = attraction.getPictures().get(0);
        }
        ImageLoader.loadImgFromUrl(
                binding.cover,
                cover,
                R.drawable.ic_placeholder_attraction_cover,
                R.drawable.ic_placeholder_attraction_cover
        );

        binding.name.setText(attraction.getName());

        binding.location.setText(attraction.getLocation());

        binding.score.setText(FloatFormatter.formatWithDotOne(attraction.getScore()));

        List<Integer> scoreStarColorList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (i < attraction.getScore()) {
                scoreStarColorList.add(ContextCompat.getColor(context, R.color.primary));
            } else {
                scoreStarColorList.add(ContextCompat.getColor(context, R.color.text_dark_light));
            }
        }
        binding.scoreOne.setColorFilter(scoreStarColorList.get(0));
        binding.scoreTwo.setColorFilter(scoreStarColorList.get(1));
        binding.scoreThree.setColorFilter(scoreStarColorList.get(2));
        binding.scoreFour.setColorFilter(scoreStarColorList.get(3));
        binding.scoreFive.setColorFilter(scoreStarColorList.get(4));

        binding.followerCount.setText(AttractionFormatter.formatFollowerCount(attraction.getFollowers()));

        binding.storyCount.setText(AttractionFormatter.formatStoryCountBefore(attraction.getStoryCount()));

        AttractionTagListAdapter attractionTagListAdapter = new AttractionTagListAdapter();
        binding.tagList.setAdapter(attractionTagListAdapter);
        binding.tagList.setLayoutManager(new FlexboxLayoutManager(context));
        binding.tagList.setItemAnimator(null);
        binding.tagList.addItemDecoration(attractionTagItemDecoration(context));
        attractionTagListAdapter.setRecords(attraction.getTags());
    }

    @Override
    protected void onBindNoMoreFooterViewHolder(@NonNull @NotNull NoMoreFooterViewHolder holder, int position) {
        super.onBindNoMoreFooterViewHolder(holder, position);
        ItemNoMoreFooterBinding binding = holder.binding;

        if (getRecords().isEmpty()) {
            binding.footerText.setText(R.string.hint_no_attraction);
        } else {
            binding.footerText.setText(R.string.hint_no_more_attraction);
        }
    }

    private RecyclerView.ItemDecoration attractionTagItemDecoration(Context context) {
        FlexboxItemDecoration flexboxItemDecoration = new FlexboxItemDecoration(context);
        flexboxItemDecoration.setDrawable(
                ContextCompat.getDrawable(context, R.drawable.shape_attraction_detail_tag_item_divider));
        return flexboxItemDecoration;
    }

    public static class AttractionListViewHolder extends BaseViewHolder<ItemAttractionBinding> {
        public AttractionListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        @Override
        protected ItemAttractionBinding createViewBinding(View itemView) {
            return ItemAttractionBinding.bind(itemView);
        }
    }

    public interface AttractionClickListener {
        void onAttractionClick(String attractionId);
    }
}
