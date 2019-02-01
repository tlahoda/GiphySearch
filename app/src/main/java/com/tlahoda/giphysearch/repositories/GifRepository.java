package com.tlahoda.giphysearch.repositories;

import com.tlahoda.giphysearch.models.interfaces.Model;
import com.tlahoda.giphysearch.models.interfaces.SearchResponse;
import com.tlahoda.giphysearch.providers.DataProvider;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class GifRepository<DataType extends Model, ResponseType extends SearchResponse<List<DataType>>> implements ImageRepository<DataType> {
    private static final int SEARCH_LIMIT = 20;

    private final List<DataType> gifs;

    private final DataProvider<ResponseType> dataProvider;

    private String query;

    public GifRepository(@NonNull DataProvider<ResponseType> dataProvider) {
        gifs = new ArrayList<>();

        this.dataProvider = dataProvider;

        query = "";
    }

    @Override
    public DataType get(int position) {
        return gifs.get(position);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void load(@NonNull LifecycleOwner owner, @NonNull String query) {
        this.query = query;

        loadData(owner);
    }

    @Override
    public void loadMore(@NonNull LifecycleOwner owner) {
        loadData(owner);
    }

    private void loadData(LifecycleOwner owner) {
        LiveData<ResponseType> searchResponse = dataProvider.load(query, SEARCH_LIMIT, gifs.size());

        searchResponse.observe(owner, response -> gifs.addAll(response.getData()));
    }

    @Override
    public int size() {
        return gifs.size();
    }

    @Override
    public void clear() {
        gifs.clear();
    }

    @Override
    public void cancel() {
        dataProvider.cancel();
    }
}
