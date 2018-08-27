package com.github.neone35.enalyzer.data.network;

import android.content.Context;
import android.net.Uri;
import android.support.v4.util.SparseArrayCompat;
import android.util.Pair;
import android.util.SparseArray;

import com.blankj.utilcode.util.EncryptUtils;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.github.neone35.enalyzer.HelpUtils;
import com.github.neone35.enalyzer.data.models.localjson.ClassificationResponse;
import com.github.neone35.enalyzer.data.models.remotejson.pubchemcid.PubchemCIDResponse;
import com.github.neone35.enalyzer.data.models.remotejson.pubchemdetails.PubchemDetailsResponse;
import com.github.neone35.enalyzer.data.models.remotejson.pubchemhazards.InformationItem;
import com.github.neone35.enalyzer.data.models.remotejson.pubchemhazards.PubchemHazardsResponse;
import com.github.neone35.enalyzer.data.models.remotejson.wikidatatitle.WikiDataTitlePage;
import com.github.neone35.enalyzer.data.models.remotejson.wikidatatitle.WikiDataTitleResponse;
import com.github.neone35.enalyzer.data.models.remotejson.wikimain.WikiMainPage;
import com.github.neone35.enalyzer.data.models.remotejson.wikimain.WikiMainResponse;
import com.github.neone35.enalyzer.data.models.room.Hazard;
import com.google.common.base.Joiner;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    private static final String WIKI_BASE_URL = "https://en.wikipedia.org/";
    private static final String WIKI_IMG_AUTHORITY = "upload.wikimedia.org";
    private static final String PUBCHEM_BASE_URL = "https://pubchem.ncbi.nlm.nih.gov/";
    private static final int WIKI_MAIN_TITLES_LIMIT = 50;

    // gets additive titles one by one (with qCode) and returns list of them
    public static ArrayList<String> getWikiTitleStringList(ArrayList<String> qCodes) {
        WikiEndpointInterface wikiEndpointInterface = getWikiApiService();
        ArrayList<String> additiveTitles = new ArrayList<>();
        try {
            for (String qCode : qCodes) {
                Call<WikiDataTitleResponse> retroCall =
                        wikiEndpointInterface.getAdditiveTitleByWikiQCode(qCode);
                WikiDataTitleResponse wikiDataTitleResponse = retroCall.execute().body();
                if (wikiDataTitleResponse != null) {
                    // check if title exists for this Q code
                    if (wikiDataTitleResponse.getWikiDataQuery() != null) {
                        // key = "1156934", value = WikiDataTitlePage
                        HashMap<String, WikiDataTitlePage> wikiPageMap =
                                wikiDataTitleResponse.getWikiDataQuery().getWikiPageMap();
                        // select "1156934"
                        Map.Entry<String, WikiDataTitlePage> wikiPageEntry =
                                wikiPageMap.entrySet().iterator().next();
                        // get WikiDataTitlePage
                        WikiDataTitlePage wikiDataTitlePage = wikiPageEntry.getValue();
                        // ex. "Ammonium carbonate"
                        String title = wikiDataTitlePage.getTitle();
                        additiveTitles.add(title);
                    } else {
                        Logger.d("No wikiDataTitle query received for qCode: " + qCode);
                    }
                } else {
                    Logger.d("No wikiDataTitle response received for qCode: " + qCode);
                }
            }
        } catch (IOException e) {
            Logger.d(e.getMessage());
        }
        Logger.d("First additive title : " + additiveTitles.get(0));
        return additiveTitles;
    }

    // gets bulk of wikimainpages in one request by using additive titles
    public static ArrayList<WikiMainPage> getWikiMainPageList(ArrayList<String> additiveTitles) {
        WikiEndpointInterface wikiEndpointInterface = getWikiApiService();
        ArrayList<WikiMainPage> wikiMainPageList = new ArrayList<>();
        try {
            Logger.d("Additive title size: " + additiveTitles.size());
            // wikipedia has limit of 50 titles per request
            if (additiveTitles.size() <= WIKI_MAIN_TITLES_LIMIT) {
                String titlesJoined = Joiner.on("|").join(additiveTitles);
                Call<WikiMainResponse> retroCall =
                        wikiEndpointInterface.getWikiMainByAdditiveTitle(titlesJoined);
                WikiMainResponse wikiMainResponse = retroCall.execute().body();
                // key = "1156934", value = WikiMainPage
                if (wikiMainResponse != null) {
                    HashMap<String, WikiMainPage> wikiMainPageMap =
                            wikiMainResponse.getWikiMainQuery().getWikiPageMap();
                    // select "1156934" (key) & get WikiMainPages
                    wikiMainPageList.addAll(wikiMainPageMap.values());
                }
            } else {
                ArrayList<String> additiveTitleSets = new ArrayList<>();
                int requestNum = (int) Math.ceil(additiveTitles.size() / (double) WIKI_MAIN_TITLES_LIMIT);
                // make sets of divided titles
                for (int i = 0; i < requestNum; i++) {
                    // 0, 50, 100, 150
                    int fromIndex = i * WIKI_MAIN_TITLES_LIMIT;
                    // 50, 100, 150, 200
                    int toIndex = (i + 1) * WIKI_MAIN_TITLES_LIMIT;
                    // set toIndex to max existing if it's higher than calculated (ex. 58, not 100)
                    if (toIndex > additiveTitles.size()) toIndex = additiveTitles.size();
                    List<String> additiveTitleSet = additiveTitles.subList(fromIndex, toIndex);
                    String joinedTitleSet = Joiner.on("|").join(additiveTitleSet);
                    Logger.d("Additive title set: " + joinedTitleSet);
                    additiveTitleSets.add(joinedTitleSet);
                }
                // make requests with divided title sets
                for (int i = 0; i < requestNum; i++) {
                    Call<WikiMainResponse> retroCall =
                            wikiEndpointInterface.getWikiMainByAdditiveTitle(additiveTitleSets.get(i));
                    WikiMainResponse wikiMainResponse = retroCall.execute().body();
                    // key = "1156934", value = WikiMainPage
                    if (wikiMainResponse != null) {
                        HashMap<String, WikiMainPage> wikiMainPageMap =
                                wikiMainResponse.getWikiMainQuery().getWikiPageMap();
                        // select "1156934" (key) & get WikiMainPages
                        wikiMainPageList.addAll(wikiMainPageMap.values());
                    }
                }
            }

        } catch (IOException e) {
            Logger.d(e.getMessage());
        }
        // parsed response contains last requested url titles in the top
        // we need them in the order of request url
//        Collections.reverse(wikiMainPageList);
        Logger.d("First wikiMainPage title: " + wikiMainPageList.get(0).getTitle());
        return wikiMainPageList;
    }

    // gets pubchemCIDs one by one (with additive title) and returns list of them
    public static SparseArray<String> getPubchemCIDWithTitleList(ArrayList<String> additiveTitles) {
        PubchemEndpointInterface pubchemEndpointInterface = getPubchemApiService();
        // maps integers to objects
        SparseArray<String> pubchemCIDAndTitleArray = new SparseArray<>();
        try {
            for (String title : additiveTitles) {
                if (title != null) {
                    Call<PubchemCIDResponse> retroCall =
                            pubchemEndpointInterface.getPubchemCIDByAdditiveTitle(title);
                    PubchemCIDResponse pubchemCIDResponse = retroCall.execute().body();
                    if (pubchemCIDResponse != null) {
                        // "517111" (there can be multiple CIDs)
                        int pubchemCID = pubchemCIDResponse.getIdentifierList().getCID().get(0);
                        pubchemCIDAndTitleArray.put(pubchemCID, title);
                    } else {
                        Logger.d("No pubchemCID received for additive title: " + title);
                    }
                }
            }
        } catch (IOException e) {
            Logger.d(e.getMessage());
        }
        Logger.d("First pubchemCID : " + pubchemCIDAndTitleArray.keyAt(0));
        return pubchemCIDAndTitleArray;
    }

    // gets pubchemFormulas one by one (with pubchemCID) and returns list of them
    public static SparseArray<String> getPubchemCIDWithFormulaList(ArrayList<Integer> pubchemCIDs) {
        PubchemEndpointInterface pubchemEndpointInterface = getPubchemApiService();
        // maps integers to objects
        SparseArray<String> pubchemCIDAndFormulaMap = new SparseArray<>();
        try {
            for (int pubchemCID : pubchemCIDs) {
                Call<PubchemDetailsResponse> retroCall =
                        pubchemEndpointInterface.getPubchemDetailsByCID(pubchemCID);
                PubchemDetailsResponse pubchemDetailsResponse = retroCall.execute().body();
                if (pubchemDetailsResponse != null) {
                    // "CH8N2O3"
                    String pubchemFormula = pubchemDetailsResponse.getPropertyTable().getProperties().get(0).getMolecularFormula();
                    pubchemCIDAndFormulaMap.put(pubchemCID, pubchemFormula);
                } else {
                    Logger.d("No pubchemFormula received for cID: " + pubchemCID);
                }
            }
        } catch (IOException e) {
            Logger.d(e.getMessage());
        }
        Logger.d("First pubchemFormula : " + pubchemCIDAndFormulaMap.valueAt(0));
        return pubchemCIDAndFormulaMap;
    }

    // extracts hazardCodes from reference text (with one pubchemCID) and returns list of them
    public static List<Pair<Integer, ArrayList<Hazard>>> getPubchemCIDsWithHazardsList(ArrayList<Integer> pubchemCIDs,
                                                                                       List<String> hazardCodeList,
                                                                                       List<String> hazardStatementList) {
        PubchemEndpointInterface pubchemEndpointInterface = getPubchemApiService();
        List<Pair<Integer, ArrayList<Hazard>>> pubchemCIDsWithHazardsPairList = new ArrayList<>();
        ArrayList<Hazard> foundHazardsList = new ArrayList<>();
        try {
            for (int pubchemCID : pubchemCIDs) {
                Call<PubchemHazardsResponse> retroCall =
                        pubchemEndpointInterface.getPubchemHazardsByCID(pubchemCID);
                PubchemHazardsResponse pubchemHazardsResponse = retroCall.execute().body();
                if (pubchemHazardsResponse != null) {
                    List<InformationItem> informationItems = pubchemHazardsResponse.getRecord()
                            // "Safety and Hazards"
                            .getSection().get(0)
                            // "Hazards Identification"
                            .getSection().get(0)
                            // "GHS Classification"
                            .getSection().get(0)
                            .getInformation();
                    // search for local hazard codes in received reference texts
                    for (InformationItem informationItem : informationItems) {
                        String referenceText = informationItem.getStringValue();
                        // iterate through every hazard code
                        for (int i = 0; i < hazardCodeList.size(); i++) {
                            String hazardCode = hazardCodeList.get(i);
                            if (referenceText.contains(hazardCode)) {
                                Hazard hazard = new Hazard(hazardCode, hazardStatementList.get(i));
                                foundHazardsList.add(hazard);
                            }
                        }
                        Pair<Integer, ArrayList<Hazard>> pubchemCIDWithHazardsPair = new Pair<>(pubchemCID, foundHazardsList);
                        pubchemCIDsWithHazardsPairList.add(pubchemCIDWithHazardsPair);
                    }
                } else {
                    Logger.d("No pubchemDetails (with hazards) received for cID: " + pubchemCID);
                }
            }
        } catch (IOException e) {
            Logger.d(e.getMessage());
        }
        return pubchemCIDsWithHazardsPairList;
    }

    private static WikiEndpointInterface getWikiApiService() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WIKI_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(WikiEndpointInterface.class);
    }

    private static PubchemEndpointInterface getPubchemApiService() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PUBCHEM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(PubchemEndpointInterface.class);
    }

    // Constructs bulk absolute wiki image urls by their filenames
    // imageFilename ex. Uhličitan_amonný.JPG
    public static String getWikiImgUrl(String imgFilename) {
        try {
            String MD5OfFilename = EncryptUtils.encryptMD5ToString(imgFilename).toLowerCase();
            String MD5FirstChar = MD5OfFilename.substring(0, 1); // 9
            String MD5SecondChar = MD5OfFilename.substring(1, 2); // e

            // https://upload.wikimedia.org/wikipedia/commons/9/9e
            String imgUrl = new Uri.Builder().scheme("https")
                    .authority(WIKI_IMG_AUTHORITY)
                    .appendPath("wikipedia")
                    .appendPath("commons")
                    .appendPath(MD5FirstChar)
                    .appendPath(MD5FirstChar + MD5SecondChar)
                    .build().toString();
            //            Logger.d("First img URL: " + finalUrl);
            return imgUrl + "/" + imgFilename;
        } catch (Exception e) {
            Logger.d(e.getMessage());
            return null;
        }
    }
}
