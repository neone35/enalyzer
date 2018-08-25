package com.github.neone35.enalyzer.ui.main.scans.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.github.neone35.enalyzer.data.MainRepository;

public class ScanDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MainRepository mRepository;
    private final int mScanPhotoID;

    public ScanDetailViewModelFactory(MainRepository repository, int scanPhotoID) {
        this.mRepository = repository;
        this.mScanPhotoID = scanPhotoID;
    }

    @NonNull
    @Override
    @SuppressWarnings(value = "unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ScanDetailViewModel(mRepository, mScanPhotoID);
    }
}
