package com.tlahoda.giphysearch.models.interfaces;

public interface SearchResponse<DataType> extends Model {
    DataType getData();
}
