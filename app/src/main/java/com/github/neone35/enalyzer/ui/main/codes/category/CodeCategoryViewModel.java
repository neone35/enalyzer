package com.github.neone35.enalyzer.ui.main.codes.category;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.github.neone35.enalyzer.data.MainRepository;
import com.github.neone35.enalyzer.data.models.room.CodeCategory;

import java.util.List;

public class CodeCategoryViewModel extends ViewModel {

    private final LiveData<List<CodeCategory>> mCodeCategories;

    CodeCategoryViewModel(MainRepository repository) {
        mCodeCategories = repository.getAllCodeCategories();
    }

    public LiveData<List<CodeCategory>> getCodeCategories() {
        return mCodeCategories;
    }
}
