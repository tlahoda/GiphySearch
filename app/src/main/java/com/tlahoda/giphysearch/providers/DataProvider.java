package com.tlahoda.giphysearch.providers;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import retrofit2.Response;

public interface DataProvider<DataType> {
    LiveData<DataType> load(@NonNull String query, int limit, int offset);

    void cancel();

    interface OnDataLoaded<LoadedDataType> {
        void onDataLoaded(@NonNull Response<LoadedDataType> response);
    }

    interface OnDtaLoadFailed {
        void onFail(@NonNull Throwable t);
    }
}
