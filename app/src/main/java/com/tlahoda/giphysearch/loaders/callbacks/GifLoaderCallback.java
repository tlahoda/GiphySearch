package com.tlahoda.giphysearch.loaders.callbacks;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

public interface GifLoaderCallback<ViewModelType extends ViewModel> {
    void call(@NonNull ViewModelType viewModel);
}
