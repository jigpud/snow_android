package com.jigpud.snow.page.fooddetail;

import androidx.annotation.NonNull;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.ItemNoMoreFooterBinding;
import com.jigpud.snow.page.common.adapter.AttractionListAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class FoodAttractionListAdapter extends AttractionListAdapter {
    public FoodAttractionListAdapter(long pageSize, AttractionClickListener clickListener) {
        super(pageSize, clickListener);
    }

    @Override
    protected void onBindNoMoreFooterViewHolder(@NonNull @NotNull NoMoreFooterViewHolder holder, int position) {
        super.onBindNoMoreFooterViewHolder(holder, position);
        ItemNoMoreFooterBinding binding = holder.binding;

        if (getRecords().isEmpty()) {
            binding.footerText.setText(R.string.hint_no_food_attraction);
        } else {
            binding.footerText.setText(R.string.hint_no_more_attraction);
        }
    }
}
