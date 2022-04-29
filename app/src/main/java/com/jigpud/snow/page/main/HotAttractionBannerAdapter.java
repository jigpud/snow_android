package com.jigpud.snow.page.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.jigpud.snow.R;
import com.jigpud.snow.database.entity.AttractionEntity;
import com.jigpud.snow.databinding.HotAttractionBinding;
import com.jigpud.snow.page.base.BaseViewHolder;
import com.jigpud.snow.util.format.AttractionFormatter;
import com.jigpud.snow.util.format.FloatFormatter;
import com.jigpud.snow.util.img.ImageLoader;
import com.youth.banner.adapter.BannerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public class HotAttractionBannerAdapter extends
        BannerAdapter<AttractionEntity, HotAttractionBannerAdapter.HotAttractionBannerViewHolder> {
    private final AttractionClickListener clickListener;

    public HotAttractionBannerAdapter(List<AttractionEntity> hotAttractionList, AttractionClickListener clickListener) {
        super(hotAttractionList);
        this.clickListener = clickListener;
    }

    @Override
    public HotAttractionBannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_attraction, parent, false);
        return new HotAttractionBannerViewHolder(view);
    }

    @Override
    public void onBindView(HotAttractionBannerViewHolder holder, AttractionEntity data, int position, int size) {
        HotAttractionBinding binding = holder.binding;
        String cover = "";
        if (!data.getPhotos().isEmpty()) {
            cover = data.getPhotos().get(0);
        }

        binding.getRoot().setOnClickListener(target -> clickListener.onAttractionClick(data.getAttractionId()));

        ImageLoader.loadImgFromUrl(
                binding.cover,
                cover,
                R.drawable.ic_placeholder_attraction_cover,
                R.drawable.ic_placeholder_attraction_cover
        );

        binding.name.setText(data.getName());

        binding.location.setText(data.getLocation());

        binding.score.setText(FloatFormatter.formatWithDotOne(data.getScore()));

        binding.storyCount.setText(AttractionFormatter.formatStoryCount(data.getStoryCount()));

        Context context = binding.getRoot().getContext();
        List<Integer> scoreStarColorList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (i < data.getScore()) {
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
    }

    public static class HotAttractionBannerViewHolder extends BaseViewHolder<HotAttractionBinding> {
        public HotAttractionBannerViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        @Override
        protected HotAttractionBinding createViewBinding(View itemView) {
            return HotAttractionBinding.bind(itemView);
        }
    }

    public interface AttractionClickListener {
        void onAttractionClick(String attractionId);
    }
}
