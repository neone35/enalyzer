package com.github.neone35.enalyzer.data;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.neone35.enalyzer.AppExecutors;
import com.github.neone35.enalyzer.data.database.AdditiveDao;
import com.github.neone35.enalyzer.data.database.CodeCategoryDao;
import com.github.neone35.enalyzer.data.database.ScanPhotoDao;
import com.github.neone35.enalyzer.data.models.remotejson.wikimain.WikiMainPage;
import com.github.neone35.enalyzer.data.models.room.Additive;
import com.github.neone35.enalyzer.data.models.room.CodeCategory;
import com.github.neone35.enalyzer.data.models.room.ScanPhoto;
import com.github.neone35.enalyzer.data.network.NetworkRoot;
import com.github.neone35.enalyzer.data.network.NetworkUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MainRepository sInstance;
    private final NetworkRoot mNetworkRoot;
    private final AppExecutors mExecutors;

    private final ScanPhotoDao mScanPhotoDao;
    private final CodeCategoryDao mCodeCategoryDao;
    private final AdditiveDao mAdditiveDao;

    private ArrayList<String> mEmptyEcodes = new ArrayList<>();

    private MainRepository(ScanPhotoDao scanPhotoDao, CodeCategoryDao codeCategoryDao,
                           AdditiveDao additiveDao, NetworkRoot recipesNetworkRoot,
                           AppExecutors executors) {
        mScanPhotoDao = scanPhotoDao;
        mCodeCategoryDao = codeCategoryDao;
        mAdditiveDao = additiveDao;
        mNetworkRoot = recipesNetworkRoot;
        mExecutors = executors;

        // get notified and insert wikipedia data on download completion
        LiveData<List<WikiMainPage>> downloadedWikiData = mNetworkRoot.getDownloadedWikiMainPageList();
        downloadedWikiData.observeForever(newWikiData -> {
            mExecutors.diskIO().execute(() -> {
                ArrayList<Additive> newAdditives = new ArrayList<>();
                if (newWikiData != null) {
                    for (int i = 0; i < newWikiData.size(); i++) {
                        WikiMainPage wikiPage = newWikiData.get(i);
                        String imgUrl = NetworkUtils.getWikiImgUrl(wikiPage.getWikiMainPageProps().getPageImageFree());
                        // get existing additive by q code to match before updating
                        Additive additive = additiveDao.getOneStaticByQcode(wikiPage.getWikiMainPageProps().getWikibaseItem());
                        additive.setWikiEditDate(wikiPage.getTouched());
                        additive.setName(wikiPage.getTitle());
                        additive.setDescription(wikiPage.getExtract());
                        additive.setKnownAs(wikiPage.getWikiMainPageTerms().getAlias());
                        additive.setImageURL(imgUrl);
                        newAdditives.add(additive);
                    }
//                    Additive[] additives = newAdditives.toArray(new Additive[newAdditives.size()]);
                    mAdditiveDao.bulkUpdate(newAdditives);
                }
            });
        });
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

    // called from ScanDetailViewModel
    public LiveData<ScanPhoto> getScanPhotoById(int id) {
        mExecutors.diskIO().execute(() -> {
            ScanPhoto scanPhoto = mScanPhotoDao.getOneStaticById(id);
            List<Additive> scanPhotoAdditives = mAdditiveDao
                    .getBulkStaticByEcode(Objects.requireNonNull(scanPhoto).getECodes());
            // check if additive has empty fields
            ArrayList<String> emptyQCodes = new ArrayList<>();
            for (int i = 0; i < scanPhotoAdditives.size(); i++) {
                Additive additive = scanPhotoAdditives.get(i);
                // find additives with empty wiki fields
                String wikiQCode = additive.getWikiDataQCode();
                if (checkWikiFields(additive) && wikiQCode != null) {
                    // Q codes are used for empty field further fetch
                    emptyQCodes.add(wikiQCode);
                }
            }
            if (!emptyQCodes.isEmpty()) {
                mNetworkRoot.startWikiFetchJobService(emptyQCodes);
            }
        });
        return mScanPhotoDao.getById(id);
    }

    private boolean checkWikiFields(Additive additive) {
        return additive.getKnownAs() == null ||
                additive.getName() == null ||
                additive.getDescription() == null ||
                additive.getImageURL() == null ||
                additive.getWikiEditDate() == null;
    }

    // called from CodeCategoryViewModel
    public LiveData<List<CodeCategory>> getAllCodeCategories() {
        return mCodeCategoryDao.getAll();
    }

    // called from CodeDetailViewModel
    public LiveData<CodeCategory> getCodeCategoryById(int id) {
        switch (id) {
            case 1:
                return mCodeCategoryDao.loadColours();
            case 2:
                return mCodeCategoryDao.loadPreservatives();
            case 3:
                return mCodeCategoryDao.loadAntioxidants();
            case 4:
                return mCodeCategoryDao.loadThickeners();
            case 5:
                return mCodeCategoryDao.loadAcidityRegulators();
            case 6:
                return mCodeCategoryDao.loadFlavourEnhancers();
            case 7:
                return mCodeCategoryDao.loadAntibiotics();
            case 8:
                return mCodeCategoryDao.loadGlazingAgents();
            case 9:
                return mCodeCategoryDao.loadAdditional();
            default:
                return null;
        }
    }

    public static void initializeLocalData() {
        // initialize (& fetch) once per lifetime
    }

}
