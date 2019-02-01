package com.tlahoda.giphysearch.viewmodels;

import android.text.Html;
import android.text.Spanned;

import com.tlahoda.giphysearch.models.GifObject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

public class GifDetailViewModel extends ViewModel {
    private static final String CAPTION_FORMAT = "<b>Caption:</b> %s";
    private static final String TITLE_FORMAT = "<b>Title:</b> %s";
    private static final String WEBSITE_FORMAT = "<b>Website:</b> %s";
    private static final String USER_FORMAT = "<b>User:</b> %s";
    private static final String DEFAULT_MESSAGE = "None";

    private final GifObject gifObject;

    public GifDetailViewModel(@NonNull GifObject gifObject) {
        this.gifObject = gifObject;
    }

    public String getUrl() {
        return gifObject.getUrl();
    }

    public Spanned getCaption() {
        return Html.fromHtml(String.format(CAPTION_FORMAT, chooseDefaultOrGiven(gifObject.getCaption())), Html.FROM_HTML_MODE_LEGACY);
    }

    public Spanned getTitle() {
        return Html.fromHtml(String.format(TITLE_FORMAT, chooseDefaultOrGiven(gifObject.getTitle())), Html.FROM_HTML_MODE_LEGACY);
    }

    public Spanned getWebsite() {
        return Html.fromHtml(String.format(WEBSITE_FORMAT, chooseDefaultOrGiven(gifObject.getSource())), Html.FROM_HTML_MODE_LEGACY);
    }

    public Spanned getUsername() {
        return Html.fromHtml(String.format(USER_FORMAT, chooseDefaultOrGiven(gifObject.getUsername())), Html.FROM_HTML_MODE_LEGACY);
    }

    private String chooseDefaultOrGiven(String text) {
        if (textIsEmpty(text)) {
            text = DEFAULT_MESSAGE;
        }

        return text;
    }

    //added to not use TextUtils for testing
    private boolean textIsEmpty(String text) {
        return text == null || text.trim().length() == 0;
    }
}
