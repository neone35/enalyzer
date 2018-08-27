package com.github.neone35.enalyzer.data.network;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.util.SparseArray;

import com.github.neone35.enalyzer.AppExecutors;
import com.github.neone35.enalyzer.data.models.remotejson.wikimain.WikiMainPage;
import com.github.neone35.enalyzer.data.models.room.Hazard;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class NetworkRoot {

    // Singleton instantiation
    private static final Object LOCK = new Object();
    @SuppressLint("StaticFieldLeak")
    private static NetworkRoot sInstance;

    // MutableLiveData with expected return type to notify all observers with postValue
    private final MutableLiveData<List<String>> mDownloadedWikiTitles;
    private final MutableLiveData<List<WikiMainPage>> mDownloadedWikiMainPages;
    private final MutableLiveData<SparseArray<String>> mDownloadedPubchemCIDsWithTitles;
    private final MutableLiveData<SparseArray<String>> mDownloadedPubchemCIDsWithFormulas;
    private final MutableLiveData<List<Pair<Integer, ArrayList<Hazard>>>> mDownloadedPubchemCIDsWithHazardsList;
    private final AppExecutors mExecutors;
    private final Context mContext;
    private MutableLiveData<Boolean> mIsWikiLoading;
    private MutableLiveData<Boolean> mIsPubchemLoading;
    private MutableLiveData<Boolean> mIsSomethingLoading;

    public static final String KEY_WIKI_QCODES = "wiki_qcodes";
    public static final String KEY_HAZARD_CODES = "hazard_codes";
    public static final String KEY_HAZARD_STATEMENTS = "hazard_statements";
    public static final String KEY_ADDITIVE_TITLES = "additive_titles";

    private NetworkRoot(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;
        mDownloadedWikiTitles = new MutableLiveData<>();
        mDownloadedWikiMainPages = new MutableLiveData<>();
        mDownloadedPubchemCIDsWithTitles = new MutableLiveData<>();
        mDownloadedPubchemCIDsWithFormulas = new MutableLiveData<>();
        mDownloadedPubchemCIDsWithHazardsList = new MutableLiveData<>();
        mIsWikiLoading = new MutableLiveData<>();
        mIsPubchemLoading = new MutableLiveData<>();
        mIsSomethingLoading = new MutableLiveData<>();
    }

    // Get singleton for this class
    public static NetworkRoot getInstance(Context context, AppExecutors executors) {
//        Logger.d("Getting the network data source");
        // Only one instance of this class can be created
        if (sInstance == null) {
            // and only one thread can access this method at a time for data consistency
            synchronized (LOCK) {
                sInstance = new NetworkRoot(context.getApplicationContext(), executors);
                Logger.d("Made new network data source");
            }
        }
        return sInstance;
    }

    public LiveData<List<String>> getDownloadedWikiTitleList() {
        return mDownloadedWikiTitles;
    }

    public LiveData<List<WikiMainPage>> getDownloadedWikiMainPageList() {
        return mDownloadedWikiMainPages;
    }

    public LiveData<SparseArray<String>> getDownloadedPubchemCIDsWithTitles() {
        return mDownloadedPubchemCIDsWithTitles;
    }

    public LiveData<SparseArray<String>> getDownloadedPubchemCIDsWithFormulas() {
        return mDownloadedPubchemCIDsWithFormulas;
    }

    public LiveData<List<Pair<Integer, ArrayList<Hazard>>>> getDownloadedPubchemCIDsWithHazards() {
        return mDownloadedPubchemCIDsWithHazardsList;
    }

    public LiveData<Boolean> getWikiLoadingStatus() {
        return mIsWikiLoading;
    }

    public LiveData<Boolean> getPubchemLoadingStatus() {
        return mIsPubchemLoading;
    }

    public LiveData<Boolean> getLoadingStatus() {
        return mIsSomethingLoading;
    }


    // starts WikiJobService which calls fetchWiki() in BG
    public void startWikiFetchJobService(ArrayList<String> qCodes) {
        mIsWikiLoading.postValue(true);
        mIsSomethingLoading.postValue(true);
        Intent intentToWikiFetch = new Intent(mContext, WikiJobService.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(KEY_WIKI_QCODES, qCodes);
        intentToWikiFetch.putExtras(bundle);
        mContext.startService(intentToWikiFetch);
        Logger.d("Wiki JobService created");
    }

    // called from jobService (before GUI shows up, in BG)
    public void fetchWiki(ArrayList<String> qCodes) {
        Logger.d("Wiki fetch started");
        mExecutors.networkIO().execute(() -> {
            try {
                ArrayList<String> wikiTitleStringList = NetworkUtils.getWikiTitleStringList(qCodes);
                Logger.d("Wiki titles: " + wikiTitleStringList);
                ArrayList<WikiMainPage> wikiMainPageList = NetworkUtils.getWikiMainPageList(wikiTitleStringList);

                // notify observers of MutableLiveData (repository) if fetch is successful
                if (wikiMainPageList != null && wikiMainPageList.size() != 0) {
                    Logger.d("fetchWiki has " + wikiMainPageList.size() + " values");
                    Logger.d("First value is %s", wikiMainPageList.get(0).getTitle());
                    // update LiveData off main thread -> to main thread (postValue)
                    mDownloadedWikiMainPages.postValue(wikiMainPageList);
                    mIsWikiLoading.postValue(false);
                    mIsSomethingLoading.postValue(false);
                }
            } catch (Exception e) {
                Logger.e(e.getMessage());
            }
        });
    }

    // starts PubchemJobService which calls fetchPubchem() in BG
    public void startPubchemFetchJobService(ArrayList<String> additiveTitles, ArrayList<String> hazardCodeList,
                                            ArrayList<String> hazardStatementList) {
        mIsPubchemLoading.postValue(true);
        mIsSomethingLoading.postValue(true);
        Intent intentToPubchemFetch = new Intent(mContext, PubchemJobService.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(KEY_ADDITIVE_TITLES, additiveTitles);
        bundle.putStringArrayList(KEY_HAZARD_CODES, hazardCodeList);
        bundle.putStringArrayList(KEY_HAZARD_STATEMENTS, hazardStatementList);
        intentToPubchemFetch.putExtras(bundle);
        mContext.startService(intentToPubchemFetch);
        Logger.d("Pubchem JobService created");
    }

    // called from jobService (before GUI shows up, in BG)
    public void fetchPubchem(ArrayList<String> additiveTitles, ArrayList<String> hazardCodeList,
                             ArrayList<String> hazardStatementList) {
        Logger.d("Wiki fetch started");
        mExecutors.networkIO().execute(() -> {
            try {
                SparseArray<String> pubchemCIDWithTitleArray = NetworkUtils.getPubchemCIDWithTitleList(additiveTitles);
                // extract CIDs from SparseArray
                ArrayList<Integer> pubchemCIDs = new ArrayList<>();
                for (int i = 0; i < pubchemCIDWithTitleArray.size(); i++) {
                    pubchemCIDs.add(pubchemCIDWithTitleArray.keyAt(i));
                }
                Logger.d("Pubchem CIDs: " + pubchemCIDs);
                // use CIDs for further calls
                SparseArray<String> pubchemCIDWithFormulaArray = NetworkUtils.getPubchemCIDWithFormulaList(pubchemCIDs);
                List<Pair<Integer, ArrayList<Hazard>>> pubchemCIDsWithHazardsPairList =
                        NetworkUtils.getPubchemCIDsWithHazardsList(pubchemCIDs, hazardCodeList, hazardStatementList);

                Logger.d("pubchemCIDs has " + pubchemCIDs.size() + " values with first " + pubchemCIDs.get(0));
                Logger.d("pubchemFormulas has " + pubchemCIDWithFormulaArray.size() + " values with first " + pubchemCIDWithFormulaArray.valueAt(0));
                Logger.d("pubchemHazards has " + pubchemCIDsWithHazardsPairList.get(0).first + " values with first " + pubchemCIDsWithHazardsPairList.get(0).second.get(0).getStatementCode());
                // notify observers of MutableLiveData (repository) if fetch is successful
                if (pubchemCIDs.size() != 0 && pubchemCIDWithFormulaArray.size() != 0 && pubchemCIDsWithHazardsPairList.size() != 0) {
                    // update LiveData off main thread -> to main thread (postValue)
                    mDownloadedPubchemCIDsWithTitles.postValue(pubchemCIDWithTitleArray);
                    mDownloadedPubchemCIDsWithFormulas.postValue(pubchemCIDWithFormulaArray);
                    mDownloadedPubchemCIDsWithHazardsList.postValue(pubchemCIDsWithHazardsPairList);
                    Logger.d("finished notifying all pubchem observers");
                    mIsPubchemLoading.postValue(false);
                    mIsSomethingLoading.postValue(false);
                }
            } catch (Exception e) {
                Logger.e(e.getMessage());
            }
        });
    }
}
