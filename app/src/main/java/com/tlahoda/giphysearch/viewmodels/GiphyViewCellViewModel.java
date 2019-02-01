package com.tlahoda.giphysearch.viewmodels;

import com.tlahoda.giphysearch.models.GifObject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

public class GiphyViewCellViewModel extends ViewModel {
    private final GifObject gifObject;

    public GiphyViewCellViewModel(@NonNull GifObject gifObject) {
        this.gifObject = gifObject;
    }

    public GifObject getGifObject() {
        return gifObject;
    }

    public String getTitle() {
        return gifObject.getTitle();
    }
}
