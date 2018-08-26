package com.github.neone35.enalyzer.ui.main.scans.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.github.neone35.enalyzer.data.MainRepository;
import com.github.neone35.enalyzer.data.models.room.Additive;
import com.github.neone35.enalyzer.data.models.room.ScanPhoto;

import java.util.List;

public class ScanAdditivesVM extends ViewModel {

    private final LiveData<List<Additive>> mAdditives;
    private final LiveData<ScanPhoto> mScanPhoto;

    ScanAdditivesVM(MainRepository repository, int scanPhotoID, List<String> ecodes) {
        // download empty additive fields for this scan photo
        mScanPhoto = repository.getScanPhotoById(scanPhotoID, ecodes);
        // get live additives by scan photo ecodes
        mAdditives = repository.getBulkAdditiveByEcodes(ecodes);
    }

    public LiveData<List<Additive>> getAdditives() {
        return mAdditives;
    }

    public LiveData<ScanPhoto> getScanPhoto() {
        return mScanPhoto;
    }
}
