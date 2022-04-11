package com.jigpud.snow.page.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.jigpud.snow.R;
import com.jigpud.snow.database.entity.SearchHistoryEntity;
import com.jigpud.snow.databinding.ItemSearchHistoryBinding;
import com.jigpud.snow.page.base.BaseAdapter;
import com.jigpud.snow.page.base.BaseViewHolder;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class SearchHistoryAdapter extends BaseAdapter<SearchHistoryEntity, SearchHistoryAdapter.SearchHistoryViewHolder> {
    private final SearchHistoryClickListener clickListener;

    public SearchHistoryAdapter(SearchHistoryClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @NotNull
    @Override
    public SearchHistoryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history, parent, false);
        return new SearchHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchHistoryViewHolder holder, int position) {
        SearchHistoryEntity searchHistoryEntity = getRecord(position);
        holder.binding.history.setText(searchHistoryEntity.getKeyWords());
        holder.binding.history.setOnClickListener(target ->
                clickListener.onSearchHistoryClick(searchHistoryEntity.getKeyWords()));
        holder.binding.history.setOnCloseIconClickListener(target ->
                clickListener.onDeleteSearchHistoryClick(searchHistoryEntity.getKeyWords()));
    }

    @Override
    protected boolean areItemsTheSame(SearchHistoryEntity oldRecord, SearchHistoryEntity newRecord) {
        return oldRecord.getKeyWords().equals(newRecord.getKeyWords());
    }

    @Override
    protected boolean areContentsTheSame(SearchHistoryEntity oldRecord, SearchHistoryEntity newRecord) {
        return oldRecord.equals(newRecord);
    }

    public static class SearchHistoryViewHolder extends BaseViewHolder<ItemSearchHistoryBinding> {
        public SearchHistoryViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        @Override
        protected ItemSearchHistoryBinding createViewBinding(View itemView) {
            return ItemSearchHistoryBinding.bind(itemView);
        }
    }

    public interface SearchHistoryClickListener {
        void onSearchHistoryClick(String history);

        void onDeleteSearchHistoryClick(String history);
    }
}
