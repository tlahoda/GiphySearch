package com.tlahoda.giphysearch.repositories;

import com.tlahoda.giphysearch.models.interfaces.Model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

public interface ImageRepository<DataType extends Model> {
    DataType get(int position);

    void load(@NonNull LifecycleOwner owner, @NonNull String query);

    void loadMore(@NonNull LifecycleOwner owner);

    int size();

    void clear();

    void cancel();
}
