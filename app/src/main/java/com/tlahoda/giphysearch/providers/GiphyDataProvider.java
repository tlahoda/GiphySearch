package com.tlahoda.giphysearch.providers;

import com.tlahoda.giphysearch.BuildConfig;
import com.tlahoda.giphysearch.apis.GiphyService;
import com.tlahoda.giphysearch.models.ErrorResponse;
import com.tlahoda.giphysearch.models.GiphySearchResponse;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GiphyDataProvider implements DataProvider<GiphySearchResponse> {
    private final OnDataLoaded<GiphySearchResponse> onDataLoaded;

    private final OnDtaLoadFailed onDataLoadFailed;

    private Retrofit retrofit;

    private final GiphyService giphyService;

    private Call<GiphySearchResponse> call;

    public GiphyDataProvider(@NonNull OnDataLoaded<GiphySearchResponse> onDataLoaded, @NonNull OnDtaLoadFailed onDataLoadFailed) {
        this.onDataLoaded = onDataLoaded;
        this.onDataLoadFailed = onDataLoadFailed;

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.HTTPS_API_GIPHY_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        giphyService = retrofit.create(GiphyService.class);
    }

    @Override
    public LiveData<GiphySearchResponse> load(@NonNull String query, int limit, int offset) {
        final MutableLiveData<GiphySearchResponse> data = new MutableLiveData<>();

        giphyService.getSearchResults(query, limit, offset, BuildConfig.GIPHY_API_KEY).enqueue(new Callback<GiphySearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<GiphySearchResponse> call, @NonNull Response<GiphySearchResponse> response) {
                GiphyDataProvider.this.call = call;

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        data.setValue(response.body());

                        //noinspection unchecked
                        if (GiphyDataProvider.this.onDataLoaded != null) {
                            GiphyDataProvider.this.onDataLoaded.onDataLoaded(response);
                        }
                    }
                } else {
                    if (GiphyDataProvider.this.onDataLoadFailed != null) {
                        Converter<ResponseBody, ErrorResponse> errorConverter = retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);

                        try {
                            ErrorResponse error = errorConverter.convert(response.errorBody());

                            GiphyDataProvider.this.onDataLoadFailed.onFail(new RuntimeException(error.getMessage()));

                        } catch (IOException excpt) {
                            GiphyDataProvider.this.onDataLoadFailed.onFail(excpt);
                        }
                    }
                }

                GiphyDataProvider.this.call = null;
            }

            @Override
            public void onFailure(@NonNull Call<GiphySearchResponse> call, @NonNull Throwable t) {
                data.setValue(null);

                if (GiphyDataProvider.this.onDataLoadFailed != null) {
                    GiphyDataProvider.this.onDataLoadFailed.onFail(t);
                }

                GiphyDataProvider.this.call = null;
            }
        });

        return data;
    }

    @Override
    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }
}
