package com.jigpud.snow.page.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.jigpud.snow.R;
import com.jigpud.snow.database.entity.FoodEntity;
import com.jigpud.snow.databinding.ItemFoodVerticalBinding;
import com.jigpud.snow.databinding.ItemNoMoreFooterBinding;
import com.jigpud.snow.page.base.BaseViewHolder;
import com.jigpud.snow.util.img.ImageLoader;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class VerticalFoodListAdapter extends NoMoreFooterAdapter<FoodEntity, VerticalFoodListAdapter.FoodListViewHolder> {
    private final FoodClickListener clickListener;

    public VerticalFoodListAdapter(long pageSize, FoodClickListener clickListener) {
        super(pageSize);
        this.clickListener = clickListener;
    }
    @Override
    protected FoodListViewHolder onCreateRecordViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_vertical, parent, false);
        return new FoodListViewHolder(view);
    }

    @Override
    protected void onBindRecordViewHolder(@NonNull @NotNull FoodListViewHolder holder, int position) {
        FoodEntity food = getRecord(position);
        ItemFoodVerticalBinding binding = holder.binding;

        binding.food.setOnClickListener(target -> clickListener.onFoodClick(food.getFoodId()));

        String cover = "";
        if (food.getPictures() != null && !food.getPictures().isEmpty()) {
            cover = food.getPictures().get(0);
        }
        ImageLoader.loadImgFromUrl(
                binding.cover,
                cover,
                R.drawable.ic_placeholder_food_cover,
                R.drawable.ic_placeholder_food_cover
        );

        binding.name.setText(food.getName());

        binding.description.setText(food.getDescription());
    }

    @Override
    protected void onBindNoMoreFooterViewHolder(@NonNull @NotNull NoMoreFooterViewHolder holder, int position) {
        super.onBindNoMoreFooterViewHolder(holder, position);
        ItemNoMoreFooterBinding binding = holder.binding;

        if (getRecords().isEmpty()) {
            binding.footerText.setText(R.string.hint_no_food);
        } else {
            binding.footerText.setText(R.string.hint_no_more_food);
        }
    }

    public static class FoodListViewHolder extends BaseViewHolder<ItemFoodVerticalBinding> {
        public FoodListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        @Override
        protected ItemFoodVerticalBinding createViewBinding(View itemView) {
            return ItemFoodVerticalBinding.bind(itemView);
        }
    }

    public interface FoodClickListener {
        void onFoodClick(String foodId);
    }
}
