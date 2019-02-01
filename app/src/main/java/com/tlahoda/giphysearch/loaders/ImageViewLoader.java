package com.tlahoda.giphysearch.loaders;

import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

public interface ImageViewLoader<ViewModelType extends ViewModel> {
    void load(@NonNull ImageView imageView, @NonNull ViewModelType viewModel, @NonNull String url);
}
