package com.jigpud.snow.page.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jigpud.snow.R;
import com.jigpud.snow.databinding.ItemNoMoreFooterBinding;
import com.jigpud.snow.page.base.BaseAdapter;
import com.jigpud.snow.page.base.BaseViewHolder;
import com.jigpud.snow.util.logger.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author : jigpud
 */
public abstract class NoMoreFooterAdapter<RECORD, RECORD_VH extends RecyclerView.ViewHolder> extends BaseAdapter<RECORD, RecyclerView.ViewHolder> {
    private static final String TAG = "NoMoreFooterAdapter";

    protected static final int TYPE_RECORD = 0;
    protected static final int TYPE_FOOTER = 1;

    private boolean haveMore = true;

    protected final long pageSize;

    public NoMoreFooterAdapter(long pageSize) {
        this.pageSize = pageSize;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_RECORD) {
            return onCreateRecordViewHolder(parent, viewType);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_no_more_footer, parent, false);
            return new NoMoreFooterViewHolder(view);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_RECORD) {
            onBindRecordViewHolder((RECORD_VH) holder, position);
        } else {
            onBindNoMoreFooterViewHolder((NoMoreFooterViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getItemCount() - 1) {
            return TYPE_RECORD;
        } else {
            return TYPE_FOOTER;
        }
    }

    @Override
    public void setRecords(List<RECORD> records) {
        super.setRecords(records);
        this.haveMore = records.size() == pageSize;
        notifyItemChanged(getItemCount() - 1);
    }

    @Override
    public void addRecords(List<RECORD> records) {
        super.addRecords(records);
        this.haveMore = records.size() == pageSize;
        notifyItemChanged(getItemCount() - 1);
    }

    protected abstract RECORD_VH onCreateRecordViewHolder(@NonNull @NotNull ViewGroup parent, int viewType);

    protected abstract void onBindRecordViewHolder(@NonNull @NotNull RECORD_VH holder, int position);

    protected void onBindNoMoreFooterViewHolder(@NonNull @NotNull NoMoreFooterViewHolder holder, int position) {
        Logger.d(TAG, "haveMore: %s", Boolean.toString(haveMore));
        if (haveMore) {
            holder.binding.getRoot().setVisibility(View.GONE);
        } else {
            holder.binding.getRoot().setVisibility(View.VISIBLE);
        }
    }

    public boolean haveRecords() {
        return super.getItemCount() > 0;
    }

    public static class NoMoreFooterViewHolder extends BaseViewHolder<ItemNoMoreFooterBinding> {
        public NoMoreFooterViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        @Override
        protected ItemNoMoreFooterBinding createViewBinding(View itemView) {
            return ItemNoMoreFooterBinding.bind(itemView);
        }
    }
}
