package com.tlahoda.giphysearch.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.InvocationTargetException;

public class SingleArgumentViewModelFactory<ArgType> implements ViewModelProvider.Factory {
    private final Class argTypeClass;
    private final ArgType arg;

    public SingleArgumentViewModelFactory(@NonNull ArgType arg, @NonNull Class argTypeClass) {
        this.argTypeClass = argTypeClass;
        this.arg = arg;
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass.getConstructor(argTypeClass).newInstance(arg);

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException excpt) {
            excpt.printStackTrace();
        }

        return null;
    }
}
