package com.github.neone35.enalyzer.data;

import android.arch.lifecycle.LiveData;

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

public class MainRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MainRepository sInstance;
    private final NetworkRoot mNetworkRoot;
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

    // gets list of Additives by Ecodes
    // used in ScanDetailListFragment && CodeDetailListFragment
    public LiveData<List<Additive>> getBulkAdditiveByEcodes(List<String> ecodes) {
        return mAdditiveDao.getBulkByEcode(ecodes);
    }

    public LiveData<Boolean> getNetworkLoadingStatus() {
        return mNetworkRoot.getLoadingStatus();
    }

    // checks and downloads empty Additive fields using ScanPhoto eCodes (Additive PK)
    // called from ScanDetailViewModel
    public LiveData<ScanPhoto> fetchAndGetScanPhotoById(int id, List<String> ecodes) {
        mExecutors.diskIO().execute(() -> {
            List<Additive> scanPhotoAdditives = mAdditiveDao.getBulkStaticByEcode(ecodes);
            // check if additive has empty fields
            ArrayList<String> emptyQCodes = checkEmptyAdditivesByQCode(scanPhotoAdditives);
            if (!emptyQCodes.isEmpty()) {
                mNetworkRoot.startWikiFetchJobService(emptyQCodes);
            }
        });
        return mScanPhotoDao.getById(id);
    }

    private ArrayList<String> checkEmptyAdditivesByQCode(List<Additive> scanPhotoAdditives) {
        ArrayList<String> emptyQCodes = new ArrayList<>();
        for (int i = 0; i < scanPhotoAdditives.size(); i++) {
            Additive additive = scanPhotoAdditives.get(i);
            // find additives with empty wiki fields
            String wikiQCode = additive.getWikiDataQCode();
            // only additives with assigned Q code will be fetched
            if (checkWikiFields(additive) && wikiQCode != null) {
                // Q codes are used for further empty field fetch
                emptyQCodes.add(wikiQCode);
            }
        }
        return emptyQCodes;
    }

    private boolean checkWikiFields(Additive additive) {
        return additive.getKnownAs() == null ||
                additive.getName() == null ||
                additive.getDescription() == null ||
                additive.getImageURL() == null ||
                additive.getWikiEditDate() == null;
    }

    // called from CodeCategoryVM
    public LiveData<List<CodeCategory>> getAllCodeCategories() {
        return mCodeCategoryDao.getAll();
    }

    // called from CodeDetailViewModel
    public LiveData<CodeCategory> fetchAndGetCodeCategoryById(int id, List<String> ecodes) {
        // fetch empty fields of requested category
        mExecutors.diskIO().execute(() -> {
            List<Additive> scanPhotoAdditives = mAdditiveDao.getBulkStaticByEcode(ecodes);
            // check if additive has empty fields
            ArrayList<String> emptyQCodes = checkEmptyAdditivesByQCode(scanPhotoAdditives);
            if (!emptyQCodes.isEmpty()) {
                mNetworkRoot.startWikiFetchJobService(emptyQCodes);
            }
        });
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
