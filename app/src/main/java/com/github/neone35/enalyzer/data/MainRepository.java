package com.github.neone35.enalyzer.data;

import android.arch.lifecycle.LiveData;

import com.github.neone35.enalyzer.AppExecutors;
import com.github.neone35.enalyzer.HelpUtils;
import com.github.neone35.enalyzer.data.database.AdditiveDao;
import com.github.neone35.enalyzer.data.database.CodeCategoryDao;
import com.github.neone35.enalyzer.data.database.ScanPhotoDao;
import com.github.neone35.enalyzer.data.models.localjson.ecodelist.EcodeListItem;
import com.github.neone35.enalyzer.data.models.room.ScanPhoto;
import com.github.neone35.enalyzer.data.network.NetworkRoot;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class MainRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MainRepository sInstance;
    private final NetworkRoot mRecipesNetworkRoot;
    private final AppExecutors mExecutors;

    private final ScanPhotoDao mScanPhotoDao;
    private final CodeCategoryDao mCodeCategoryDao;
    private final AdditiveDao mAdditiveDao;

    private MainRepository(ScanPhotoDao scanPhotoDao, CodeCategoryDao codeCategoryDao,
                           AdditiveDao additiveDao, NetworkRoot recipesNetworkRoot,
                           AppExecutors executors) {
        mScanPhotoDao = scanPhotoDao;
        mCodeCategoryDao = codeCategoryDao;
        mAdditiveDao = additiveDao;
        mRecipesNetworkRoot = recipesNetworkRoot;
        mExecutors = executors;
    }

    public synchronized static MainRepository getInstance(
            ScanPhotoDao scanPhotoDao, CodeCategoryDao codeCategoryDao,
            AdditiveDao additiveDao, NetworkRoot recipesNetworkRoot,
            AppExecutors executors) {
//        Logger.d("Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MainRepository(scanPhotoDao, codeCategoryDao, additiveDao,
                        recipesNetworkRoot, executors);
                Logger.d("Made new repository");
            }
        }
        return sInstance;
    }

    // called from ScanPhotosViewModel
    public LiveData<List<ScanPhoto>> getAllScanPhotos() {
        return mScanPhotoDao.getAll();
    }

    // called from ?
    public LiveData<ScanPhoto> getScanPhotoById(int id) {
        return mScanPhotoDao.getById(id);
    }

    public static void initializeLocalData() {
        // initialize (& fetch) once per lifetime
    }

}
