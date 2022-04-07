package com.jigpud.snow.page.base;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public abstract class BaseViewHolder<VB extends ViewBinding> extends RecyclerView.ViewHolder {
    public final VB binding;

    public BaseViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        binding = createViewBinding(itemView);
    }

    protected abstract VB createViewBinding(View itemView);
}
