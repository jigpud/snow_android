package com.jigpud.snow.page.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.jigpud.snow.R;
import com.jigpud.snow.SnowApplication;
import com.jigpud.snow.bean.UserInformationResponse;
import com.jigpud.snow.databinding.ItemNoMoreFooterBinding;
import com.jigpud.snow.databinding.ItemUserBinding;
import com.jigpud.snow.page.base.BaseViewHolder;
import com.jigpud.snow.util.img.ImageLoader;
import com.jigpud.snow.util.user.CurrentUser;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class UserListAdapter extends NoMoreFooterAdapter<UserInformationResponse, UserListAdapter.UserListViewHolder> {
    private final UserClickListener clickListener;

    public UserListAdapter(long pageSize, UserClickListener clickListener) {
        super(pageSize);
        this.clickListener = clickListener;
    }

    @Override
    protected UserListViewHolder onCreateRecordViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserListViewHolder(view);
    }

    @Override
    protected void onBindRecordViewHolder(@NonNull @NotNull UserListViewHolder holder, int position) {
        UserInformationResponse user = getRecord(position);
        ItemUserBinding binding = holder.binding;

        binding.getRoot().setOnClickListener(target -> clickListener.onUserClick(user.getUserid()));

        ImageLoader.loadImgFromUrl(
                binding.avatar,
                user.getAvatar(),
                R.drawable.ic_placeholder_avatar,
                R.drawable.ic_placeholder_avatar
        );

        binding.nickname.setText(user.getNickname());

        binding.signature.setText(user.getSignature());

        if (user.getUserid().equals(CurrentUser.getInstance(SnowApplication.getAppContext()).getCurrentUserid())) {
            binding.follow.setVisibility(View.GONE);
        } else {
            binding.follow.setOnClickListener(target -> {
                if (user.getHaveFollowed()) {
                    clickListener.onUnfollow(user.getUserid());
                } else {
                    clickListener.onFollow(user.getUserid());
                }
            });
            Context context = binding.getRoot().getContext();
            int followButtonColor = ContextCompat.getColor(context, R.color.item_user_follow_btn_bg_follow);
            int followButtonTextRes = R.string.item_user_follow;
            if (user.getHaveFollowed()) {
                followButtonColor = ContextCompat.getColor(context, R.color.item_user_follow_btn_bg_unfollow);
                followButtonTextRes = R.string.item_user_unfollow;
            }
            binding.follow.setText(followButtonTextRes);
            binding.follow.setBackgroundColor(followButtonColor);
            binding.follow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onBindNoMoreFooterViewHolder(@NonNull @NotNull NoMoreFooterViewHolder holder, int position) {
        super.onBindNoMoreFooterViewHolder(holder, position);
        ItemNoMoreFooterBinding binding = holder.binding;
        binding.footerText.setText(R.string.hint_no_more_user);
    }

    @Override
    protected boolean areItemsTheSame(UserInformationResponse oldRecord, UserInformationResponse newRecord) {
        return oldRecord.getUserid().equals(newRecord.getUserid());
    }

    @Override
    protected boolean areContentsTheSame(UserInformationResponse oldRecord, UserInformationResponse newRecord) {
        return oldRecord.equals(newRecord);
    }

    public static class UserListViewHolder extends BaseViewHolder<ItemUserBinding> {
        public UserListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        @Override
        protected ItemUserBinding createViewBinding(View itemView) {
            return ItemUserBinding.bind(itemView);
        }
    }

    public interface UserClickListener {
        void onUserClick(String userid);

        void onFollow(String userid);

        void onUnfollow(String userid);
    }
}
