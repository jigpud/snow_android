package com.jigpud.snow.page.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.jigpud.snow.R;
import com.jigpud.snow.database.entity.FoodEntity;
import com.jigpud.snow.databinding.ItemFoodHorizontalBinding;
import com.jigpud.snow.page.base.BaseAdapter;
import com.jigpud.snow.page.base.BaseViewHolder;
import com.jigpud.snow.util.img.ImageLoader;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class HorizontalFoodListAdapter extends BaseAdapter<FoodEntity, HorizontalFoodListAdapter.FoodListViewHolder> {
    private final FoodClickListener clickListener;

    public HorizontalFoodListAdapter(FoodClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @NotNull
    @Override
    public FoodListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_horizontal, parent, false);
        return new FoodListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FoodListViewHolder holder, int position) {
        FoodEntity food = getRecord(position);
        ItemFoodHorizontalBinding binding = holder.binding;

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

    public static class FoodListViewHolder extends BaseViewHolder<ItemFoodHorizontalBinding> {
        public FoodListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        @Override
        protected ItemFoodHorizontalBinding createViewBinding(View itemView) {
            return ItemFoodHorizontalBinding.bind(itemView);
        }
    }

    public interface FoodClickListener {
        void onFoodClick(String foodId);
    }
}
