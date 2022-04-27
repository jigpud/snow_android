package com.jigpud.snow.page.storydetail;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.jigpud.snow.R;
import com.jigpud.snow.bean.CommentResponse;
import com.jigpud.snow.databinding.ItemCommentBinding;
import com.jigpud.snow.databinding.ItemNoMoreFooterBinding;
import com.jigpud.snow.page.base.BaseViewHolder;
import com.jigpud.snow.page.common.adapter.NoMoreFooterAdapter;
import com.jigpud.snow.util.format.DateFormatter;
import com.jigpud.snow.util.format.IntegerFormatter;
import com.jigpud.snow.util.img.ImageLoader;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class CommentListAdapter extends NoMoreFooterAdapter<CommentResponse, CommentListAdapter.CommentListViewHolder> {
    private final CommentClickListener clickListener;

    public CommentListAdapter(long pageSize, CommentClickListener clickListener) {
        super(pageSize);
        this.clickListener = clickListener;
    }

    @Override
    protected CommentListViewHolder onCreateRecordViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentListViewHolder(view);
    }

    @Override
    protected void onBindRecordViewHolder(@NonNull @NotNull CommentListViewHolder holder, int position) {
        CommentResponse comment = getRecord(position);
        ItemCommentBinding binding = holder.binding;

        ImageLoader.loadImgFromUrl(
                binding.avatar,
                comment.getAuthorAvatar(),
                R.drawable.ic_placeholder_avatar,
                R.drawable.ic_placeholder_avatar
        );
        binding.avatar.setOnClickListener(target -> clickListener.onAuthorClick(comment.getAuthorId()));

        binding.nickname.setText(comment.getAuthorNickname());

        binding.content.setText(comment.getContent());

        if (DateFormatter.getYear(System.currentTimeMillis()) == DateFormatter.getYear(comment.getCommentTime())) {
            binding.commentTime.setText(DateFormatter.monthDayHourMinute(comment.getCommentTime()));
        } else {
            binding.commentTime.setText(DateFormatter.yearMonthDayHourMinute(comment.getCommentTime()));
        }

        Context context = binding.getRoot().getContext();
        int likesColor = ContextCompat.getColor(context, R.color.text_dark_light);
        if (comment.isLiked()) {
            likesColor = ContextCompat.getColor(context, R.color.primary);
        }
        binding.likes.setIconTint(ColorStateList.valueOf(likesColor));
        binding.likes.setOnClickListener(target -> {
            if (comment.isLiked()) {
                clickListener.onUnlikeComment(comment.getCommentId());
            } else {
                clickListener.onLikeComment(comment.getCommentId());
            }
        });
        binding.likesCount.setText(IntegerFormatter.formatWithUnit(comment.getLikes()));
        binding.likesCount.setTextColor(likesColor);
    }

    @Override
    protected void onBindNoMoreFooterViewHolder(@NonNull @NotNull NoMoreFooterViewHolder holder, int position) {
        super.onBindNoMoreFooterViewHolder(holder, position);
        ItemNoMoreFooterBinding binding = holder.binding;
        if (getRecords().isEmpty()) {
            binding.footerText.setText(R.string.hint_no_comment);
        } else {
            binding.footerText.setText(R.string.hint_no_more_comment);
        }
    }

    @Override
    protected boolean areItemsTheSame(CommentResponse oldRecord, CommentResponse newRecord) {
        return oldRecord.getCommentId().equals(newRecord.getCommentId());
    }

    @Override
    protected boolean areContentsTheSame(CommentResponse oldRecord, CommentResponse newRecord) {
        return oldRecord.equals(newRecord);
    }

    public static class CommentListViewHolder extends BaseViewHolder<ItemCommentBinding> {
        public CommentListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        @Override
        protected ItemCommentBinding createViewBinding(View itemView) {
            return ItemCommentBinding.bind(itemView);
        }
    }

    public interface CommentClickListener {
        void onAuthorClick(String userid);

        void onLikeComment(String commentId);

        void onUnlikeComment(String commentId);
    }
}
