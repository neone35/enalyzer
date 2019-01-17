package com.github.neone35.enalyzer.ui.main.scans.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.neone35.enalyzer.data.MainRepository;
import com.github.neone35.enalyzer.data.models.room.Additive;
import com.github.neone35.enalyzer.data.models.room.ScanPhoto;

import java.util.List;

public class ScanAdditivesVM extends ViewModel {

    private final LiveData<List<Additive>> mAdditives;
    private final LiveData<ScanPhoto> mScanPhoto;
    private LiveData<Boolean> mLoading;

    ScanAdditivesVM(MainRepository repository, int scanPhotoID, List<String> ecodes) {
        // download empty additive fields for this scan photo
        mScanPhoto = repository.fetchAndGetScanPhotoById(scanPhotoID, ecodes);
        // get live additives by scan photo ecodes
        mAdditives = repository.getBulkAdditiveByEcodes(ecodes);
        mLoading = repository.getNetworkLoadingStatus();
    }

    public LiveData<List<Additive>> getAdditives() {
        return mAdditives;
    }

    public LiveData<ScanPhoto> getScanPhoto() {
        return mScanPhoto;
    }

    public LiveData<Boolean> getLoading() {
        return mLoading;
    }
}
