package com.tlahoda.giphysearch.models;

import com.google.gson.annotations.SerializedName;
import com.tlahoda.giphysearch.models.interfaces.Model;

import org.parceler.Parcel;

@Parcel
public class GifObject implements Model {
    protected String caption;
    protected String title;
    protected String source;
    protected String username;

    @SerializedName("images")
    protected ImagesModel imagesModel;

    public String getUrl() {
        return imagesModel.getOriginalImageModel().getUrl();
    }

    public String getCaption() {
        return caption;
    }

    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public String getUsername() {
        return username;
    }

    public ImagesModel getImagesModel() {
        return imagesModel;
    }
}
