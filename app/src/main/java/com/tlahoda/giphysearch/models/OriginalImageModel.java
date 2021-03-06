package com.tlahoda.giphysearch.models;

import com.tlahoda.giphysearch.models.interfaces.Model;

import org.parceler.Parcel;

@Parcel
public class OriginalImageModel implements Model {
    protected String url;

    public String getUrl() {
        return url;
    }
}
