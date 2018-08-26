package com.github.neone35.enalyzer.ui.main.scans.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.github.neone35.enalyzer.data.MainRepository;

import java.util.List;

public class ScanAdditivesVMF extends ViewModelProvider.NewInstanceFactory {

    private final MainRepository mRepository;
    private final List<String> mEcodes;
    private final int mScanPhotoID;

    public ScanAdditivesVMF(MainRepository repository, int scanPhotoID, List<String> ecodes) {
        this.mRepository = repository;
        this.mEcodes = ecodes;
        this.mScanPhotoID = scanPhotoID;
    }

    @NonNull
    @Override
    @SuppressWarnings(value = "unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ScanAdditivesVM(mRepository, mScanPhotoID, mEcodes);
    }
}
