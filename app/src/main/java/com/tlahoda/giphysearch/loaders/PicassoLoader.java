package com.tlahoda.giphysearch.loaders;

import android.net.Uri;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tlahoda.giphysearch.R;
import com.tlahoda.giphysearch.loaders.callbacks.GifLoaderCallback;

public class PicassoLoader<ViewModelType extends ViewModel, CallbackType extends GifLoaderCallback> implements ImageViewLoader<ViewModelType> {
    private final CallbackType onSuccess;
    private final CallbackType onError;

    public PicassoLoader(@NonNull CallbackType onSuccess, @NonNull CallbackType onError) {
        this.onSuccess = onSuccess;
        this.onError = onError;
    }

    @Override
    public void load(@NonNull ImageView imageView, @NonNull ViewModelType viewModel, @NonNull String url) {
        Picasso.get()
                .load(Uri.parse(url))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.baseline_error_black_18dp)
                .into(imageView, new Callback() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess() {
                        PicassoLoader.this.onSuccess.call(viewModel);
                  }

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onError(Exception e) {
                        PicassoLoader.this.onError.call(viewModel);
                    }
                });
    }
}
