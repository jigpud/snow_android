package com.jigpud.snow.page.attractiondetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.ItemAttractionTagBinding;
import com.jigpud.snow.page.base.BaseAdapter;
import com.jigpud.snow.page.base.BaseViewHolder;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class AttractionTagListAdapter extends BaseAdapter<String, AttractionTagListAdapter.AttractionTagListViewHolder> {
    @NonNull
    @NotNull
    @Override
    public AttractionTagListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attraction_tag, parent, false);
        return new AttractionTagListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AttractionTagListViewHolder holder, int position) {
        String tag = getRecord(position);
        ItemAttractionTagBinding binding = holder.binding;
        binding.tag.setText(tag);
    }

    public static class AttractionTagListViewHolder extends BaseViewHolder<ItemAttractionTagBinding> {
        public AttractionTagListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        @Override
        protected ItemAttractionTagBinding createViewBinding(View itemView) {
            return ItemAttractionTagBinding.bind(itemView);
        }
    }
}
