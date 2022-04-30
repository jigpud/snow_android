package com.jigpud.snow.page.moments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.jigpud.snow.R;
import com.jigpud.snow.database.entity.UserEntity;
import com.jigpud.snow.databinding.ItemRecommendUserBinding;
import com.jigpud.snow.page.base.BaseAdapter;
import com.jigpud.snow.page.base.BaseViewHolder;
import com.jigpud.snow.util.format.UserFormatter;
import com.jigpud.snow.util.img.ImageLoader;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class RecommendUserListAdapter extends BaseAdapter<UserEntity, RecommendUserListAdapter.RecommendUserListViewHolder> {
    private final UserClickListener clickListener;

    public RecommendUserListAdapter(UserClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @NotNull
    @Override
    public RecommendUserListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_user, parent, false);
        return new RecommendUserListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecommendUserListViewHolder holder, int position) {
        UserEntity user = getRecord(position);
        ItemRecommendUserBinding binding = holder.binding;

        binding.getRoot().setOnClickListener(target -> clickListener.onUserClick(user.getUserid()));

        ImageLoader.loadImgFromUrl(
                binding.avatar,
                user.getAvatar(),
                R.drawable.ic_placeholder_avatar,
                R.drawable.ic_placeholder_avatar
        );

        binding.nickname.setText(user.getNickname());

        binding.signature.setText(user.getSignature());

        binding.userAbstract.setText(UserFormatter.getAbstract(user.getStoryCount(), user.getLikes()));
    }

    public static class RecommendUserListViewHolder extends BaseViewHolder<ItemRecommendUserBinding> {
        public RecommendUserListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        @Override
        protected ItemRecommendUserBinding createViewBinding(View itemView) {
            return ItemRecommendUserBinding.bind(itemView);
        }
    }

    public interface UserClickListener {
        void onUserClick(String userid);
    }
}
