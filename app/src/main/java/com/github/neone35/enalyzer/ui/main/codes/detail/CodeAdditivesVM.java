package com.github.neone35.enalyzer.ui.main.codes.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.neone35.enalyzer.data.MainRepository;
import com.github.neone35.enalyzer.data.models.room.Additive;
import com.github.neone35.enalyzer.data.models.room.CodeCategory;

import java.util.List;

public class CodeAdditivesVM extends ViewModel {

    private final LiveData<List<Additive>> mAdditives;
    private final LiveData<CodeCategory> mCodeCategory;
    private LiveData<Boolean> mLoading;

    CodeAdditivesVM(MainRepository repository, int codeCategoryID, List<String> ecodes) {
        // download empty additive fields for this scan photo
        mCodeCategory = repository.fetchAndGetCodeCategoryById(codeCategoryID, ecodes);
        // get live additives by scan photo ecodes
        mAdditives = repository.getBulkAdditiveByEcodes(ecodes);
        mLoading = repository.getNetworkLoadingStatus();
    }

    public LiveData<List<Additive>> getAdditives() {
        return mAdditives;
    }

    public LiveData<CodeCategory> getCodeCategory() {
        return mCodeCategory;
    }

    public LiveData<Boolean> getLoading() {
        return mLoading;
    }

}
