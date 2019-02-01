package com.tlahoda.giphysearch.listeners;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class SearchResultsScrollListener extends RecyclerView.OnScrollListener {
    private static final int THRESHOLD = 10;

    private int currentTotal;
    private boolean loading;

    private final Runnable callback;

    public SearchResultsScrollListener(@NonNull Runnable callback) {
        this.callback = callback;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (recyclerView.getLayoutManager() != null) {
            int visibleCount = recyclerView.getChildCount();
            int itemCount = recyclerView.getLayoutManager().getItemCount();

            int[] positions = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPositions(null);

            int firstVisibleItem = 0;

            if (positions != null && positions.length > 0) {
                firstVisibleItem = positions[0];
            }

            if (loading && itemCount > currentTotal) {
                loading = false;
                currentTotal = itemCount;
            }

            if (!loading && (itemCount - visibleCount) <= (firstVisibleItem + THRESHOLD)) {
                callback.run();

                loading = true;
            }
        }
    }

    public void reset() {
        loading = false;
        currentTotal = 0;
    }
}
