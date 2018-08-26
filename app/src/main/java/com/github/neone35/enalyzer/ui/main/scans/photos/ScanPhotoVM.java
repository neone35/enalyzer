package com.github.neone35.enalyzer.ui.main.scans.photos;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.github.neone35.enalyzer.data.MainRepository;
import com.github.neone35.enalyzer.data.models.room.ScanPhoto;

import java.util.List;

class ScanPhotoVM extends ViewModel {

    private final LiveData<List<ScanPhoto>> mScanPhotos;

    ScanPhotoVM(MainRepository repository) {
        mScanPhotos = repository.getAllScanPhotos();
    }

    public LiveData<List<ScanPhoto>> getScanPhotos() {
        return mScanPhotos;
    }

}
