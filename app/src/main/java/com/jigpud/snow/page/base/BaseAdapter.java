package com.jigpud.snow.page.base;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jigpud
 */
public abstract class BaseAdapter<RECORD, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private static final String TAG = "BaseAdapter";

    private final List<RECORD> records = new ArrayList<>();
    protected RecyclerView target;

    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        target = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        target = null;
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void setRecords(List<RECORD> records) {
        List<RECORD> oldRecords = new ArrayList<>(this.records);
        List<RECORD> newRecords = new ArrayList<>(records);
        notifyDiff(oldRecords, newRecords);
    }

    public void addRecords(List<RECORD> records) {
        List<RECORD> oldRecords = new ArrayList<>(this.records);
        List<RECORD> newRecords = new ArrayList<>(this.records);
        newRecords.addAll(records);
        notifyDiff(oldRecords, newRecords);
    }

    public void updateRecord(RECORD record) {
        List<RECORD> oldRecords = new ArrayList<>(this.records);
        List<RECORD> newRecords = new ArrayList<>(this.records);
        for (int i = 0; i < newRecords.size(); i++) {
            if (areItemsTheSame(newRecords.get(i), record)) {
                newRecords.set(i, record);
                break;
            }
        }
        notifyDiff(oldRecords, newRecords);
    }

    public void deleteRecord(RECORD record) {
        List<RECORD> oldRecords = new ArrayList<>(this.records);
        List<RECORD> newRecords = new ArrayList<>(this.records);
        for (int i = 0; i < newRecords.size(); i++) {
            if (areItemsTheSame(newRecords.get(i), record)) {
                newRecords.remove(i);
                break;
            }
        }
        notifyDiff(oldRecords, newRecords);
    }

    public List<RECORD> getRecords() {
        return records;
    }

    protected RECORD getRecord(int position) {
        return records.get(position);
    }

    protected boolean areItemsTheSame(RECORD oldRecord, RECORD newRecord) {
        return false;
    }

    protected  boolean areContentsTheSame(RECORD oldRecord, RECORD newRecord) {
        return false;
    }

    private void notifyDiff(List<RECORD> oldRecords, List<RECORD> newRecords) {
        this.records.clear();
        this.records.addAll(newRecords);
        DiffCallback diffCallback = new DiffCallback(oldRecords, newRecords);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        diffResult.dispatchUpdatesTo(this);
    }

    private class DiffCallback extends DiffUtil.Callback {
        private final List<RECORD> oldRecords;
        private final List<RECORD> newRecords;

        public DiffCallback(List<RECORD> oldRecords, List<RECORD> newRecords) {
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
