package com.jigpud.snow.page.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.jigpud.snow.R;
import com.jigpud.snow.bean.UserInformationResponse;
import com.jigpud.snow.databinding.ItemNoMoreFooterBinding;
import com.jigpud.snow.databinding.ItemUserBinding;
import com.jigpud.snow.page.base.BaseViewHolder;
import com.jigpud.snow.util.img.ImageLoader;
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


    }

    @Override
    protected void onBindNoMoreFooterViewHolder(@NonNull @NotNull NoMoreFooterViewHolder holder, int position) {
        super.onBindNoMoreFooterViewHolder(holder, position);
        ItemNoMoreFooterBinding binding = holder.binding;
        binding.footerText.setText(R.string.hint_no_more_user);
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
