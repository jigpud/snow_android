package com.jigpud.snow.page.common.itemdecoration;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.jigpud.snow.util.pixel.PixelUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private final int spacingSize;

    public GridSpacingItemDecoration(int spacingSizeDp) {
        spacingSize = (int) PixelUtil.dpToPixel(spacingSizeDp);
    }

    @Override
    public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int spanCount;
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }else {
            spanCount = 1;
        }

        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;

        outRect.left = column * spacingSize / spanCount;
        outRect.right = spacingSize - (column + 1) * spacingSize / spanCount;
        if (position >= spanCount) {
            outRect.top = spacingSize;
        }
    }
}
