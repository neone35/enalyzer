package com.github.neone35.enalyzer.data.network;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.github.neone35.enalyzer.AppExecutors;
import com.github.neone35.enalyzer.data.models.remotejson.wikimain.WikiMainPage;
import com.github.neone35.enalyzer.data.models.room.Additive;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class NetworkRoot {

    // Singleton instantiation
    private static final Object LOCK = new Object();
    @SuppressLint("StaticFieldLeak")
    private static NetworkRoot sInstance;

    // MutableLiveData with expected return type to notify all observers with postValue
    private final MutableLiveData<List<String>> mDownloadedWikiTitles;
    private final MutableLiveData<List<WikiMainPage>> mDownloadedWikiMainPages;
    private final MutableLiveData<List<Integer>> mDownloadedPubchemCIDs;
    private final MutableLiveData<List<String>> mDownloadedPubchemFormulas;
    private final MutableLiveData<HashMap<String, String>> mDownloadedPubchemHazards;
    private final AppExecutors mExecutors;
    private final Context mContext;

    public static final String KEY_WIKI_QCODES = "wiki_qcodes";

    private NetworkRoot(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;
        mDownloadedWikiTitles = new MutableLiveData<>();
        mDownloadedWikiMainPages = new MutableLiveData<>();
        mDownloadedPubchemCIDs = new MutableLiveData<>();
        mDownloadedPubchemFormulas = new MutableLiveData<>();
        mDownloadedPubchemHazards = new MutableLiveData<>();
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

    public LiveData<List<Integer>> getDownloadedPubchemCIDs() {
        return mDownloadedPubchemCIDs;
    }

    public LiveData<List<String>> getDownloadedPubchemFormulas() {
        return mDownloadedPubchemFormulas;
    }

    public LiveData<HashMap<String, String>> getDownloadedPubchemHazards() {
        return mDownloadedPubchemHazards;
    }

    public void startWikiFetchJobService(ArrayList<String> qCodes) {
        Intent intentToWikiFetch = new Intent(mContext, WikiJobService.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(KEY_WIKI_QCODES, qCodes);
        intentToWikiFetch.putExtras(bundle);
        mContext.startService(intentToWikiFetch);
        Logger.d("Wiki JobService created");
    }

    // calls fetchWiki from jobService (before GUI shows up, in BG)
    public void fetchWiki(ArrayList<String> qCodes) {
        Logger.d("Wiki fetch started");
        mExecutors.networkIO().execute(() -> {
            try {
                ArrayList<String> wikiTitleStringList = NetworkUtils.getWikiTitleStringList(qCodes);
                ArrayList<WikiMainPage> wikiMainPageList = NetworkUtils.getWikiMainPageList(wikiTitleStringList);

                // notify observers of MutableLiveData (repository) if fetch is successful
                if (wikiMainPageList != null && wikiMainPageList.size() != 0) {
                    mDownloadedWikiTitles.postValue(wikiTitleStringList);
                    Logger.d("fetchWiki has " + wikiMainPageList.size() + " values");
                    Logger.d("First value is %s", wikiMainPageList.get(0).getTitle());
                    // update LiveData off main thread -> to main thread (postValue)
                    mDownloadedWikiMainPages.postValue(wikiMainPageList);
                }
            } catch (Exception e) {
                Logger.e(e.getMessage());
            }
        });
    }
}
