package com.tlahoda.giphysearch.models;

import com.tlahoda.giphysearch.models.interfaces.SearchResponse;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class GiphySearchResponse implements SearchResponse<List<GifObject>> {
    protected List<GifObject> data;

    public List<GifObject> getData() {
        return data;
    }
}
