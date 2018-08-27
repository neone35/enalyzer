package com.github.neone35.enalyzer.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.util.SparseArray;

import com.github.neone35.enalyzer.AppExecutors;
import com.github.neone35.enalyzer.data.database.AdditiveDao;
import com.github.neone35.enalyzer.data.database.CodeCategoryDao;
import com.github.neone35.enalyzer.data.database.HazardDao;
import com.github.neone35.enalyzer.data.database.ScanPhotoDao;
import com.github.neone35.enalyzer.data.models.remotejson.wikimain.WikiMainPage;
import com.github.neone35.enalyzer.data.models.room.Additive;
import com.github.neone35.enalyzer.data.models.room.CodeCategory;
import com.github.neone35.enalyzer.data.models.room.Hazard;
import com.github.neone35.enalyzer.data.models.room.ScanPhoto;
import com.github.neone35.enalyzer.data.network.NetworkRoot;
import com.github.neone35.enalyzer.data.network.NetworkUtils;
import com.github.neone35.enalyzer.ui.main.MainActivity;
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
    private final HazardDao mHazardDao;

    private MainRepository(ScanPhotoDao scanPhotoDao, CodeCategoryDao codeCategoryDao,
                           AdditiveDao additiveDao, HazardDao hazardDao,
                           NetworkRoot recipesNetworkRoot, AppExecutors executors) {
        mScanPhotoDao = scanPhotoDao;
        mCodeCategoryDao = codeCategoryDao;
        mAdditiveDao = additiveDao;
        mHazardDao = hazardDao;
        mNetworkRoot = recipesNetworkRoot;
        mExecutors = executors;

        // get notified and insert WIKIPEDIA data on download completion
        LiveData<List<WikiMainPage>> downloadedWikiData = mNetworkRoot.getDownloadedWikiMainPageList();
        downloadedWikiData.observeForever(newWikiData -> mExecutors.diskIO().execute(() -> {
            ArrayList<Additive> newAdditives = new ArrayList<>();
            if (newWikiData != null) {
                for (int i = 0; i < newWikiData.size(); i++) {
                    WikiMainPage wikiPage = newWikiData.get(i);

                    // get existing additive by q code to match before updating
                    String currentAdditiveQcode = wikiPage.getWikiMainPageProps().getWikibaseItem();
                    Additive additive = additiveDao.getOneStaticByQcode(currentAdditiveQcode);
                    additive.setWikiEditDate(wikiPage.getTouched());
                    additive.setName(wikiPage.getTitle());
                    additive.setDescription(wikiPage.getExtract());
                    additive.setKnownAs(wikiPage.getWikiMainPageTerms().getAlias());
                    String imgUrl = NetworkUtils.getWikiImgUrl(wikiPage.getWikiMainPageProps().getPageImageFree());
                    additive.setImageURL(imgUrl);
                    newAdditives.add(additive);
                }
                mAdditiveDao.bulkUpdate(newAdditives);
            }
        }));

        // get notified and insert PUBCHEM data on download completion
        LiveData<SparseArray<String>> downloadedPubchemCIDsWithTitles = mNetworkRoot.getDownloadedPubchemCIDsWithTitles();
        downloadedPubchemCIDsWithTitles.observeForever(pubchemCIDsWithTitles -> mExecutors.diskIO().execute(() -> {
            ArrayList<Additive> newAdditives = new ArrayList<>();
            if (pubchemCIDsWithTitles != null) {
                // extract CIDs from SparseArray
                ArrayList<Integer> pubchemCIDs = new ArrayList<>();
                for (int i = 0; i < pubchemCIDsWithTitles.size(); i++) {
                    int pubchemCID = pubchemCIDsWithTitles.keyAt(i);
                    if (pubchemCID != 0)
                        pubchemCIDs.add(pubchemCID);
                }
                // extract titles from SparseArray
                ArrayList<String> titles = new ArrayList<>();
                for (int i = 0; i < pubchemCIDsWithTitles.size(); i++) {
                    String title = pubchemCIDsWithTitles.valueAt(i);
                    if (title != null)
                        titles.add(title);
                }
                // update CID using title
                for (int i = 0; i < pubchemCIDs.size(); i++) {
                    int pubchemCID = pubchemCIDs.get(i);
                    String title = titles.get(i);
                    // get existing additive by title code to match before updating
                    Additive additive = additiveDao.getOneStaticByTitle(title);
                    additive.setPubchemID(pubchemCID);
                    newAdditives.add(additive);
                }
                mAdditiveDao.bulkUpdate(newAdditives);
            }
        }));
        LiveData<SparseArray<String>> downloadedPubchemCIDsWithFormulas = mNetworkRoot.getDownloadedPubchemCIDsWithFormulas();
        downloadedPubchemCIDsWithFormulas.observeForever(pubchemCIDsWithFormulas -> mExecutors.diskIO().execute(() -> {
            ArrayList<Additive> newAdditives = new ArrayList<>();
            if (pubchemCIDsWithFormulas != null) {
                // extract CIDs from SparseArray
                ArrayList<Integer> pubchemCIDs = new ArrayList<>();
                for (int i = 0; i < pubchemCIDsWithFormulas.size(); i++) {
                    int pubchemCID = pubchemCIDsWithFormulas.keyAt(i);
                    if (pubchemCID != 0)
                        pubchemCIDs.add(pubchemCID);
                }
                // extract formulas from SparseArray
                ArrayList<String> formulas = new ArrayList<>();
                for (int i = 0; i < pubchemCIDsWithFormulas.size(); i++) {
                    String formula = pubchemCIDsWithFormulas.valueAt(i);
                    if (formula != null)
                        formulas.add(formula);
                }
                // update Formula using CID
                for (int i = 0; i < pubchemCIDs.size(); i++) {
                    int pubchemCID = pubchemCIDs.get(i);
                    String formula = formulas.get(i);
                    // get existing additive by title code to match before updating
                    Additive additive = additiveDao.getOneStaticByCID(pubchemCID);
                    additive.setFormula(formula);
                    newAdditives.add(additive);
                }
                mAdditiveDao.bulkUpdate(newAdditives);
            }
        }));
        LiveData<List<Pair<Integer, ArrayList<Hazard>>>> downloadedPubchemCIDsWithHazardsList = mNetworkRoot.getDownloadedPubchemCIDsWithHazards();
        downloadedPubchemCIDsWithHazardsList.observeForever(pubchemCIDsWithHazardsList -> mExecutors.diskIO().execute(() -> {
            ArrayList<Additive> newAdditives = new ArrayList<>();
            if (pubchemCIDsWithHazardsList != null) {
                // extract CIDs from Pair
                ArrayList<Integer> pubchemCIDs = new ArrayList<>();
                for (int i = 0; i < pubchemCIDsWithHazardsList.size(); i++) {
                    int pubchemCID = pubchemCIDsWithHazardsList.get(i).first;
                    if (pubchemCID != 0)
                        pubchemCIDs.add(pubchemCID);
                }
                // extract hazards from Pair
                ArrayList<Hazard> hazardsList = new ArrayList<>();
                for (int i = 0; i < pubchemCIDsWithHazardsList.size(); i++) {
                    int hazardsNum = pubchemCIDsWithHazardsList.get(i).second.size();
                    for (int j = 0; j < hazardsNum; j++) {
                        Hazard hazard = pubchemCIDsWithHazardsList.get(i).second.get(j);
                        if (hazard != null)
                            hazardsList.add(hazard);
                    }
                }
                // update Hazards using CID
                for (int i = 0; i < pubchemCIDs.size(); i++) {
                    int pubchemCID = pubchemCIDs.get(i);
                    Additive additive = additiveDao.getOneStaticByCID(pubchemCID);
                    additive.setHazardList(hazardsList);
                    newAdditives.add(additive);
                }
                mAdditiveDao.bulkUpdate(newAdditives);
            }
        }));
    }

    public synchronized static MainRepository getInstance(
            ScanPhotoDao scanPhotoDao, CodeCategoryDao codeCategoryDao,
            AdditiveDao additiveDao, HazardDao hazardDao,
            NetworkRoot recipesNetworkRoot, AppExecutors executors) {
//        Logger.d("Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MainRepository(scanPhotoDao, codeCategoryDao, additiveDao,
                        hazardDao, recipesNetworkRoot, executors);
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

    // gets one Additive by Ecode
    // used in AdditiveFragment
    public LiveData<Additive> getAdditiveByEcode(String eCode) {
        return mAdditiveDao.getOneByEcode(eCode);
    }

    public LiveData<Boolean> getNetworkLoadingStatus() {
        return mNetworkRoot.getLoadingStatus();
    }

    // checks and downloads empty Additive fields using ScanPhoto eCodes (Additive PK)
    // called from ScanDetailViewModel
    public LiveData<ScanPhoto> fetchAndGetScanPhotoById(int id, List<String> ecodes) {
        mExecutors.diskIO().execute(() -> {
            List<Additive> scanPhotoAdditives = mAdditiveDao.getBulkStaticByEcode(ecodes);

            List<String> hazardCodes = mHazardDao.getStaticAllStatementCodes();
            ArrayList<String> hazardCodesArrayList = new ArrayList<>(hazardCodes.size());
            hazardCodesArrayList.addAll(hazardCodes);

            List<String> hazardStatements = mHazardDao.getStaticAllStatements();
            ArrayList<String> hazardStatementsArrayList = new ArrayList<>(hazardCodes.size());
            hazardStatementsArrayList.addAll(hazardStatements);

            // check if additive has empty fields
            ArrayList<String> emptyQCodes = checkEmptyAdditiveFields(scanPhotoAdditives, MainActivity.SCANS_DETAIL);
            if (!emptyQCodes.isEmpty()) {
                mNetworkRoot.startWikiFetchJobService(emptyQCodes);
                // start pubchem fetch only after wiki fetch is finished
                // when titles will be present
                mNetworkRoot.getWikiLoadingStatus().observeForever(wikiIsLoading -> {
                    if (wikiIsLoading != null) {
                        if (!wikiIsLoading) {
                            ArrayList<String> additiveTitles = new ArrayList<>();
                            for (int i = 0; i < scanPhotoAdditives.size(); i++) {
                                additiveTitles.add(scanPhotoAdditives.get(i).getName());
                            }
                            mNetworkRoot.startPubchemFetchJobService(additiveTitles, hazardCodesArrayList, hazardStatementsArrayList);
                        }
                    }
                });
            }
        });
        return mScanPhotoDao.getById(id);
    }

    // called from CodeCategoryVM

    public LiveData<List<CodeCategory>> getAllCodeCategories() {
        return mCodeCategoryDao.getAll();
    }
    // called from CodeDetailViewModel

    public LiveData<CodeCategory> fetchAndGetCodeCategoryById(int id, List<String> ecodes) {
        // fetch empty fields of requested category
        mExecutors.diskIO().execute(() -> {
            List<Additive> codeCategoryAdditives = mAdditiveDao.getBulkStaticByEcode(ecodes);
            // check if additive has empty fields
            ArrayList<String> emptyQCodes = checkEmptyAdditiveFields(codeCategoryAdditives, MainActivity.CODES_DETAIL);
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

    private ArrayList<String> checkEmptyAdditiveFields(List<Additive> additiveList, String tabSource) {
        ArrayList<String> emptyQCodes = new ArrayList<>();
        for (int i = 0; i < additiveList.size(); i++) {
            Additive additive = additiveList.get(i);
            // find additives with empty wiki fields
            String wikiQCode = additive.getWikiDataQCode();
            if (tabSource.equals(MainActivity.SCANS_DETAIL)) {
                // only additives with assigned Q code will be fetched
                if (wikiQCode != null) {
                    // check both wiki and pubchem fields when scan details are open
                    if (checkWikiFields(additive) || checkPubchemFields(additive)) {
                        // Q codes are used for further empty field fetch
                        emptyQCodes.add(wikiQCode);
                    }
                }
            } else if (tabSource.equals(MainActivity.CODES_DETAIL)) {
                if (wikiQCode != null) {
                    // check only wiki fields when code details are open
                    if (checkWikiFields(additive)) {
                        // Q codes are used for further empty field fetch
                        emptyQCodes.add(wikiQCode);
                    }
                }
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

    private boolean checkPubchemFields(Additive additive) {
        return additive.getFormula() == null ||
                additive.getHazardList() == null;
    }

    public static void initializeLocalData() {
        // initialize (& fetch) once per lifetime
    }

}
