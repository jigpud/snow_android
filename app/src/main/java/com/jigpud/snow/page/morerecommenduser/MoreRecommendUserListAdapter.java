package com.jigpud.snow.page.morerecommenduser;

import androidx.annotation.NonNull;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.ItemNoMoreFooterBinding;
import com.jigpud.snow.page.common.adapter.UserListAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class MoreRecommendUserListAdapter extends UserListAdapter {
    public MoreRecommendUserListAdapter(long pageSize, UserClickListener clickListener) {
        super(pageSize, clickListener);
    }

    @Override
    protected void onBindNoMoreFooterViewHolder(@NonNull @NotNull NoMoreFooterViewHolder holder, int position) {
        super.onBindNoMoreFooterViewHolder(holder, position);
        ItemNoMoreFooterBinding binding = holder.binding;

        if (getRecords().isEmpty()) {
            binding.footerText.setText(R.string.hint_no_recommend_user);
        } else {
            binding.footerText.setText(R.string.hint_no_more_user);
        }
    }
}
