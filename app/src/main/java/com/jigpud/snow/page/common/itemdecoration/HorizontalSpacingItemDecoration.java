package com.jigpud.snow.page.common.itemdecoration;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jigpud.snow.util.logger.Logger;
import com.jigpud.snow.util.pixel.PixelUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author : jigpud
 */
public class HorizontalSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private final int spacingSize;
    private final int edgeSpacingSize;

    public HorizontalSpacingItemDecoration(int spacingSizeDp) {
        this(spacingSizeDp, 18);
    }

    public HorizontalSpacingItemDecoration(int spacingSizeDp, int edgeSpacingSizeDp) {
        spacingSize = (int) PixelUtil.dpToPixel(spacingSizeDp);
        edgeSpacingSize = (int) PixelUtil.dpToPixel(edgeSpacingSizeDp);
    }

    @Override
    public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager != null) {
            int itemCount = layoutManager.getItemCount();
            int position = parent.getChildAdapterPosition(view);
            if (position == 0) {
                outRect.left = edgeSpacingSize;
            } else {
                outRect.left = spacingSize;
            }
            if (position == itemCount - 1) {
                outRect.right = edgeSpacingSize;
            }
        }
    }
}
