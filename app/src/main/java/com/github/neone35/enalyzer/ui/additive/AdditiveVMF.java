package com.github.neone35.enalyzer.ui.additive;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.github.neone35.enalyzer.data.MainRepository;

public class AdditiveVMF extends ViewModelProvider.NewInstanceFactory {

    private final MainRepository mRepository;
    private final String mEcode;

    public AdditiveVMF(MainRepository repository, String eCode) {
        this.mRepository = repository;
        this.mEcode = eCode;
    }

    @NonNull
    @Override
    @SuppressWarnings(value = "unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AdditiveVM(mRepository, mEcode);
    }

}
