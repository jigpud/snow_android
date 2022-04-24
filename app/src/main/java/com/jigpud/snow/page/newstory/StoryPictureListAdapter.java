package com.jigpud.snow.page.newstory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.ItemAddPictureBinding;
import com.jigpud.snow.databinding.ItemSquarePictureBinding;
import com.jigpud.snow.page.base.BaseAdapter;
import com.jigpud.snow.page.base.BaseViewHolder;
import com.jigpud.snow.util.img.ImageLoader;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class StoryPictureListAdapter extends BaseAdapter<String, RecyclerView.ViewHolder> {
    private static final int TYPE_PICTURE = 0;
    private static final int TYPE_ADD_PICTURE = 1;

    private final StoryPictureListCallback callback;

    public StoryPictureListAdapter(StoryPictureListCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_PICTURE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_square_picture, parent, false);
            return new StoryPictureViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_picture, parent, false);
            return new AddStoryPictureViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_PICTURE) {
            String path = getRecord(position);
            ItemSquarePictureBinding binding = ((StoryPictureViewHolder) holder).binding;

            ImageLoader.loadImgFromUrl(
                    binding.picture,
                    path,
                    R.drawable.ps_image_placeholder,
                    R.drawable.ps_image_placeholder
            );
            binding.picture.setOnClickListener(target -> callback.onPictureClick(position));

            binding.delete.setOnClickListener(target -> callback.onPictureDelete(position));
        } else {
            ItemAddPictureBinding binding = ((AddStoryPictureViewHolder) holder).binding;
            binding.addPicture.setOnClickListener(target -> callback.onAddPicture());
        }
    }

    @Override
    public int getItemCount() {
        return getPictureCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getPictureCount()) {
            return TYPE_ADD_PICTURE;
        } else {
            return TYPE_PICTURE;
        }
    }

    @Override
    protected boolean areItemsTheSame(String oldRecord, String newRecord) {
        return oldRecord.equals(newRecord);
    }

    @Override
    protected boolean areContentsTheSame(String oldRecord, String newRecord) {
        return oldRecord.equals(newRecord);
    }

    private int getPictureCount() {
        return super.getItemCount();
    }

    public static class StoryPictureViewHolder extends BaseViewHolder<ItemSquarePictureBinding> {
        public StoryPictureViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        @Override
        protected ItemSquarePictureBinding createViewBinding(View itemView) {
            return ItemSquarePictureBinding.bind(itemView);
        }
    }

    public static class AddStoryPictureViewHolder extends BaseViewHolder<ItemAddPictureBinding> {
        public AddStoryPictureViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        @Override
        protected ItemAddPictureBinding createViewBinding(View itemView) {
            return ItemAddPictureBinding.bind(itemView);
        }
    }

    public interface StoryPictureListCallback {
        void onAddPicture();

        void onPictureClick(int position);

        void onPictureDelete(int position);
    }
}
