package com.github.neone35.enalyzer.data.network;

import android.content.Context;
import android.net.Uri;

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
                    Logger.d("No wikiDataTitle received for qCode: " + qCode);
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
    public static ArrayList<Integer> getPubchemCIDList(ArrayList<String> additiveTitles) {
        PubchemEndpointInterface pubchemEndpointInterface = getPubchemApiService();
        ArrayList<Integer> pubchemCIDs = new ArrayList<>();
        try {
            for (String title : additiveTitles) {
                Call<PubchemCIDResponse> retroCall =
                        pubchemEndpointInterface.getPubchemCIDByAdditiveTitle(title);
                PubchemCIDResponse pubchemCIDResponse = retroCall.execute().body();
                if (pubchemCIDResponse != null) {
                    // "517111" (there can be multiple CIDs)
                    int pubchemCID = pubchemCIDResponse.getIdentifierList().getCID().get(0);
                    pubchemCIDs.add(pubchemCID);
                } else {
                    Logger.d("No pubchemCID received for additive title: " + title);
                }
            }
        } catch (IOException e) {
            Logger.d(e.getMessage());
        }
        Logger.d("First CID : " + pubchemCIDs.get(0));
        return pubchemCIDs;
    }

    // gets pubchemFormulas one by one (with pubchemCID) and returns list of them
    public static ArrayList<String> getPubchemFormulaList(ArrayList<Integer> pubchemCIDs) {
        PubchemEndpointInterface pubchemEndpointInterface = getPubchemApiService();
        ArrayList<String> pubchemFormulas = new ArrayList<>();
        try {
            for (int pubchemCID : pubchemCIDs) {
                Call<PubchemDetailsResponse> retroCall =
                        pubchemEndpointInterface.getPubchemDetailsByCID(pubchemCID);
                PubchemDetailsResponse pubchemDetailsResponse = retroCall.execute().body();
                if (pubchemDetailsResponse != null) {
                    // "CH8N2O3"
                    String pubchemFormula = pubchemDetailsResponse.getPropertyTable().getProperties().get(0).getMolecularFormula();
                    pubchemFormulas.add(pubchemFormula);
                } else {
                    Logger.d("No pubchemFormula received for cID: " + pubchemCID);
                }
            }
        } catch (IOException e) {
            Logger.d(e.getMessage());
        }
        Logger.d("First pubchemFormula : " + pubchemFormulas.get(0));
        return pubchemFormulas;
    }

    // extracts hazardCodes from reference text (with one pubchemCID) and returns list of them
    public static HashMap<String, String> getPubchemHazardsList(int pubchemCID, Context ctx) {
        PubchemEndpointInterface pubchemEndpointInterface = getPubchemApiService();
        HashMap<String, String> pubchemHazardsMap = new HashMap<>();
        try {
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
                // match received references with local hazard codes
                for (InformationItem informationItem : informationItems) {
                    String referenceText = informationItem.getStringValue();
                    String clsJsonString = HelpUtils.readJSONStringFromAsset(ctx, "ghs_classification.json");
                    HashMap<String, ClassificationResponse> clsObjectsMap = HelpUtils.getLocalHazardObjectList(clsJsonString);
                    ArrayList<String> hazardCodeList = HelpUtils.getHazardCodeList(clsObjectsMap);
                    ArrayList<String> hazardStatementList = HelpUtils.getHazardStatementList(clsObjectsMap);
                    // iterate through every hazard code
                    for (int i = 0; i < hazardCodeList.size(); i++) {
                        String hazardCode = hazardCodeList.get(i);
                        if (referenceText.contains(hazardCode)) {
                            // put code as key and statement as value into map
                            pubchemHazardsMap.put(hazardCode, hazardStatementList.get(i));
                        }
                    }
                }
            } else {
                Logger.d("No pubchemDetails (with hazards) received for cID: " + pubchemCID);
            }
        } catch (IOException e) {
            Logger.d(e.getMessage());
        }
        String firstHazardCode = pubchemHazardsMap.entrySet().iterator().next().getKey();
        String firstHazardStatement = pubchemHazardsMap.entrySet().iterator().next().getValue();
        Logger.d("First hazard is: " + firstHazardCode + " " + firstHazardStatement);
        return pubchemHazardsMap;
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
            String finalUrl = imgUrl + "/" + imgFilename;
            Logger.d("First img URL: " + finalUrl);
            return finalUrl;
        } catch (Exception e) {
            Logger.d(e.getMessage());
            return null;
        }
    }
}
