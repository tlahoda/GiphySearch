package com.tlahoda.giphysearch.bindingadapters;

import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tlahoda.giphysearch.R;

public class GlideImageViewBindingAdapters {
    private static final float STROKE_WIDTH = 20.0f;
    private static final float CENTER_RADIUS = 150.0f;

    @BindingAdapter("glide_url")
    public static void setGlideUrl(ImageView imageView, String url) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(imageView.getContext());
        circularProgressDrawable.setStrokeWidth(STROKE_WIDTH);
        circularProgressDrawable.setCenterRadius(CENTER_RADIUS);
        circularProgressDrawable.setTint(ContextCompat.getColor(imageView.getContext(), R.color.default_tint));
        circularProgressDrawable.start();

        RequestOptions options = new RequestOptions()
                .placeholder(circularProgressDrawable)
                .error(R.drawable.baseline_error_black_18dp);

        Glide.with(imageView.getContext())
                .asGif()
                .load(url)
                .apply(options)
                .into(imageView);
    }
}
