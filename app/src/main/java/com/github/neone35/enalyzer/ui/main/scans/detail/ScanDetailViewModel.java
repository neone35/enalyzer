package com.github.neone35.enalyzer.ui.main.scans.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.github.neone35.enalyzer.data.MainRepository;
import com.github.neone35.enalyzer.data.models.room.Additive;
import com.github.neone35.enalyzer.data.models.room.ScanPhoto;

import java.util.List;

public class ScanDetailViewModel extends ViewModel {

    private final LiveData<ScanPhoto> mScanPhoto;

    ScanDetailViewModel(MainRepository repository, int scanPhotoID) {
        mScanPhoto = repository.getScanPhotoById(scanPhotoID);
    }

    public LiveData<ScanPhoto> getScanPhoto() {
        return mScanPhoto;
    }
}
