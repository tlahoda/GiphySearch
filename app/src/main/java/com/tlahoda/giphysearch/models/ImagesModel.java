package com.tlahoda.giphysearch.models;

import com.google.gson.annotations.SerializedName;
import com.tlahoda.giphysearch.models.interfaces.Model;

import org.parceler.Parcel;

@Parcel
public class ImagesModel implements Model {
    @SerializedName("original")
    protected OriginalImageModel originalImageModel;

    @SerializedName("fixed_width_still")
    protected FixedWidthStillImageModel fixedWidthStillImageModel;

    public OriginalImageModel getOriginalImageModel() {
        return originalImageModel;
    }

    public FixedWidthStillImageModel getFixedWidthStillImageModel() {
        return fixedWidthStillImageModel;
    }
}
