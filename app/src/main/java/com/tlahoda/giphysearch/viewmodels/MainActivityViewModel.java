package com.tlahoda.giphysearch.viewmodels;

import com.tlahoda.giphysearch.repositories.ImageRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private ImageRepository repository;

    public MainActivityViewModel(@NonNull ImageRepository repository) {
        this.repository = repository;
    }

    @SuppressWarnings("unused")
    public ImageRepository getRepository() {
        return repository;
    }
}
