package com.github.neone35.enalyzer.ui.main.codes.category;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.github.neone35.enalyzer.data.MainRepository;

public class CodeCategoryViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MainRepository mRepository;

    public CodeCategoryViewModelFactory(MainRepository repository) {
        this.mRepository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings(value = "unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new CodeCategoryViewModel(mRepository);
    }
}
