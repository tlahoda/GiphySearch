package com.tlahoda.giphysearch.models;

import com.google.gson.annotations.SerializedName;
import com.tlahoda.giphysearch.models.interfaces.Model;

public class ErrorResponse implements Model {
    @SerializedName("message")
    private String message;

    @SerializedName("error")
    private Error error;

    public String getMessage() {
        return message;
    }

    public Error getError() {
        return error;
    }
}
