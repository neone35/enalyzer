package com.github.neone35.enalyzer.ui.main.codes.detail;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.github.neone35.enalyzer.data.MainRepository;

import java.util.List;

public class CodeAdditivesVMF extends ViewModelProvider.NewInstanceFactory {

    private final MainRepository mRepository;
    private final List<String> mEcodes;
    private final int mCodeCategoryID;

    public CodeAdditivesVMF(MainRepository repository, int codeCategoryID, List<String> ecodes) {
        this.mRepository = repository;
        this.mEcodes = ecodes;
        this.mCodeCategoryID = codeCategoryID;
    }

    @NonNull
    @Override
    @SuppressWarnings(value = "unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new CodeAdditivesVM(mRepository, mCodeCategoryID, mEcodes);
    }

}
