package com.tlahoda.giphysearch.itemdecorations;

import android.graphics.Rect;
import android.view.View;

import com.tlahoda.giphysearch.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchResultsItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = (int) (view.getResources().getDimension(R.dimen.giphy_viewcell_start_margin));

        outRect.top = (int) (view.getResources().getDimension(R.dimen.giphy_viewcell_item_top_margin));

        outRect.right = (int) (view.getResources().getDimension(R.dimen.giphy_viewcell_end_margin));

        outRect.bottom = (int) (view.getResources().getDimension(R.dimen.giphy_viewcell_bottom_margin));
    }
}
