package com.tlahoda.giphysearch.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tlahoda.giphysearch.R;
import com.tlahoda.giphysearch.databinding.FragmentGifDetailBinding;
import com.tlahoda.giphysearch.factories.SingleArgumentViewModelFactory;
import com.tlahoda.giphysearch.models.GifObject;
import com.tlahoda.giphysearch.viewmodels.GifDetailViewModel;

import androidx.lifecycle.ViewModelProviders;
import org.parceler.Parcels;

public class GifDetailFragment extends Fragment {
    private static final String GIF_OBJECT_TAG = "gifObject";

    private GifObject gif;

    public GifDetailFragment() {
        //noop
    }

    public static GifDetailFragment newInstance(@NonNull GifObject gif) {
        GifDetailFragment fragment = new GifDetailFragment();

        Bundle args = new Bundle();
        args.putParcelable(GIF_OBJECT_TAG, Parcels.wrap(gif));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            gif = Parcels.unwrap(getArguments().getParcelable(GIF_OBJECT_TAG));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentGifDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gif_detail, container, false);

        binding.setViewModel(ViewModelProviders.of(this, new SingleArgumentViewModelFactory(gif, GifObject.class)).get(GifDetailViewModel.class));

        return binding.getRoot();
    }
}
