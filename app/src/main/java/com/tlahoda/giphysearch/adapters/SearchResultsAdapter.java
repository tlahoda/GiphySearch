package com.tlahoda.giphysearch.adapters;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.tlahoda.giphysearch.BR;
import com.tlahoda.giphysearch.R;
import com.tlahoda.giphysearch.databinding.ViewcellGiphyBinding;
import com.tlahoda.giphysearch.loaders.ImageViewLoader;
import com.tlahoda.giphysearch.models.GifObject;
import com.tlahoda.giphysearch.repositories.ImageRepository;
import com.tlahoda.giphysearch.viewmodels.GiphyViewCellViewModel;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {
    private final ImageViewLoader imageViewLoader;

    private final OnItemLongPressListener onItemLongPressListener;

    private final OnEmptyListener emptyListener;

    private final ImageRepository repository;

    public SearchResultsAdapter(@NonNull ImageRepository repository, @NonNull ImageViewLoader imageViewLoader, @NonNull OnItemLongPressListener onItemLongPressListener, @NonNull OnEmptyListener emptyListener) {
        this.repository = repository;
        this.imageViewLoader = imageViewLoader;
        this.onItemLongPressListener = onItemLongPressListener;
        this.emptyListener = emptyListener;
    }

    @NonNull
    @Override
    public SearchResultsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, viewType, parent, false);

        return new ViewHolder((ViewcellGiphyBinding) binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultsAdapter.ViewHolder holder, int position) {
        holder.bind(new GiphyViewCellViewModel((GifObject) repository.get(position)));

        new Handler(Looper.getMainLooper()).post(this::notifyDataSetChanged);
    }

    public void checkIfEmpty() {
        emptyListener.onEmpty(repository.size() == 0);
    }

    @Override
    public int getItemCount() {
        checkIfEmpty();

        return repository.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.viewcell_giphy;
    }

    @Override
    public void onViewRecycled(@NonNull SearchResultsAdapter.ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.recycle();
    }

    public void clear() {
        repository.clear();

        checkIfEmpty();

        new Handler(Looper.getMainLooper()).post(this::notifyDataSetChanged);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        private ViewcellGiphyBinding binding;

        ViewHolder(ViewcellGiphyBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

            binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    System.out.println("###############################");
                    System.out.println("on long click");

                    return true;
                }
            });

            binding.getRoot().setOnClickListener(view -> {
                SearchResultsAdapter.this.onItemLongPressListener.onItemLongPress(ViewHolder.this.binding.getViewModel());
            });
        }

        @SuppressWarnings("unchecked")
        private void bind(GiphyViewCellViewModel viewModel) {
            binding.setVariable(BR.viewModel, viewModel);

            binding.executePendingBindings();

            SearchResultsAdapter.this.imageViewLoader.load(binding.image, viewModel, viewModel.getGifObject().getImagesModel().getFixedWidthStillImageModel().getUrl());
        }

        private void recycle() {
            binding.image.setImageDrawable(null);
        }
    }

    public interface OnItemLongPressListener {
        void onItemLongPress(@NonNull GiphyViewCellViewModel viewModel);
    }

    public interface OnEmptyListener {
        void onEmpty(boolean empty);
    }
}
