package com.jigpud.snow.page.base;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public abstract class BaseAdapter<R, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private static final String TAG = "BaseAdapter";

    private final List<R> records = new ArrayList<>();

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void setRecords(List<R> records) {
        List<R> oldRecords = new ArrayList<>(this.records);
        List<R> newRecords = new ArrayList<>(records);
        notifyDiff(oldRecords, newRecords);
    }

    public void addRecords(List<R> records) {
        List<R> oldRecords = new ArrayList<>(this.records);
        List<R> newRecords = new ArrayList<>(this.records);
        newRecords.addAll(records);
        notifyDiff(oldRecords, newRecords);
    }

    protected R getRecord(int position) {
        return records.get(position);
    }

    protected boolean areItemsTheSame(R oldRecord, R newRecord) {
        return false;
    }

    protected  boolean areContentsTheSame(R oldRecord, R newRecord) {
        return false;
    }

    private void notifyDiff(List<R> oldRecords, List<R> newRecords) {
        this.records.clear();
        this.records.addAll(newRecords);
        DiffCallback diffCallback = new DiffCallback(oldRecords, newRecords);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        diffResult.dispatchUpdatesTo(this);
    }

    private class DiffCallback extends DiffUtil.Callback {
        private final List<R> oldRecords;
        private final List<R> newRecords;

        public DiffCallback(List<R> oldRecords, List<R> newRecords) {
            this.oldRecords = oldRecords;
            this.newRecords = newRecords;
        }

        @Override
        public int getOldListSize() {
            return oldRecords.size();
        }

        @Override
        public int getNewListSize() {
            return newRecords.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return BaseAdapter.this.areItemsTheSame(oldRecords.get(oldItemPosition), newRecords.get(newItemPosition));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return BaseAdapter.this.areContentsTheSame(oldRecords.get(oldItemPosition), newRecords.get(newItemPosition));
        }
    }
}
