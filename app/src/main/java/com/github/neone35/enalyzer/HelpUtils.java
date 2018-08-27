package com.github.neone35.enalyzer;

import android.content.Context;

import com.github.neone35.enalyzer.data.models.localjson.ClassificationResponse;
import com.github.neone35.enalyzer.data.models.localjson.ecodelist.EcodeListItem;
import com.github.neone35.enalyzer.data.models.localjson.ecodelist.EcodeListResponse;
import com.github.neone35.enalyzer.data.models.room.Additive;
import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpUtils {

    private static Gson gson = new Gson();
    private static int mAdditivesNum = 0;
    private static int mHazardsNum = 0;

    public static String readJSONStringFromAsset(Context ctx, String fileName) {
        String jsonString = null;
        try {
            InputStream is = ctx.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            int bytesReadNum = is.read(buffer);
            is.close();
            if (bytesReadNum > 0) {
                jsonString = new String(buffer, "UTF-8");
            } else {
                Logger.d("No bytes read from json");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return jsonString;
    }

    public static List<EcodeListItem> getLocalEcodeObjectList(String additivesJsonString) {
        Type responseType = new TypeToken<EcodeListResponse>() {
        }.getType();
        EcodeListResponse ecodeListResponse = gson.fromJson(additivesJsonString, responseType);
        return ecodeListResponse.getEcodeList();
    }

    public static ArrayList<String> getEcodes(List<EcodeListItem> ecodeObjects) {
        mAdditivesNum = ecodeObjects.size();
        ArrayList<String> eCodeList = new ArrayList<>();
        for (int i = 0; i < mAdditivesNum; i++) {
            String eCodeID = ecodeObjects.get(i).getId();
            if (eCodeID != null) {
                // "en:e322i"
                List<String> splittedId = Splitter
                        .onPattern(":")
                        .omitEmptyStrings() // remove nulls
                        .trimResults() // remove white space
                        .splitToList(eCodeID);
                // "e322i"
                String eCode = splittedId.get(1);
                // "E + 322i"
                String eCodeCapitalE = eCode.substring(0, 1).toUpperCase() + eCode.substring(1);
                eCodeList.add(eCodeCapitalE);
            }
        }
        Logger.d("Found " + eCodeList.size() + " ecodes in " + ecodeObjects.size() + " additives");
        return eCodeList;
    }

    public static ArrayList<String> getWikiDataQCodes(List<EcodeListItem> ecodeObjects) {
        ArrayList<String> wikiDataQCodeList = new ArrayList<>();
        for (int i = 0; i < mAdditivesNum; i++) {
            List<String> wikiDataURLs = ecodeObjects.get(i).getSameAs();
            if (wikiDataURLs != null) {
                String wikiDataURL = wikiDataURLs.get(0);
                // "https://www.wikidata.org/wiki/Q422071"
                List<String> splittedURL = Splitter
                        .onPattern("/")
                        .omitEmptyStrings() // remove nulls
                        .trimResults() // remove white space
                        .splitToList(wikiDataURL);
                // "Q422071"
                int splitPartsNum = splittedURL.size();
                String wikiDataQCode = splittedURL.get(splitPartsNum - 1);
                wikiDataQCodeList.add(wikiDataQCode);
            } else {
                // add null to make sure every Additive corresponds to ecode
                wikiDataQCodeList.add(null);
            }
        }
        return wikiDataQCodeList;
    }

    public static ArrayList<ClassificationResponse> getLocalHazardObjectList(String clsJsonString) {
        Type responseType = new TypeToken<Collection<ClassificationResponse>>() {
        }.getType();
        return gson.fromJson(clsJsonString, responseType);
    }

    public static ArrayList<String> getHazardCodeList(List<ClassificationResponse> clsObjectList) {
        mHazardsNum = clsObjectList.size();
        ArrayList<String> hazardCodesList = new ArrayList<>();
        for (int i = 0; i < mHazardsNum; i++) {
            // get ClassificationResponse
            ClassificationResponse clsResponse = clsObjectList.get(i);
            // "H200"
            String hazardCode = clsResponse.getCode();
            hazardCodesList.add(hazardCode);
        }
        return hazardCodesList;
    }

    public static ArrayList<String> getHazardStatementList(List<ClassificationResponse> clsObjectList) {
        mHazardsNum = clsObjectList.size();
        ArrayList<String> hazardStatementsList = new ArrayList<>();
        for (int i = 0; i < mHazardsNum; i++) {
            // get ClassificationResponse
            ClassificationResponse clsResponse = clsObjectList.get(i);
            // "Unstable Explosive""
            String hazardStatement = clsResponse.getHazardStatements();
            hazardStatementsList.add(hazardStatement);
        }
        return hazardStatementsList;
    }

    public static ArrayList<String> getWikiDataNames(List<EcodeListItem> ecodeObjects) {
        ArrayList<String> wikiDataNamesList = new ArrayList<>();
        for (int i = 0; i < mAdditivesNum; i++) {
            String wikiDataName = ecodeObjects.get(i).getName();
            if (wikiDataName != null) {
                List<String> splittedName = Splitter
                        .onPattern("-")
                        .omitEmptyStrings() // remove nulls
                        .trimResults() // remove white space
                        .splitToList(wikiDataName);
                // "Q422071"
                int splitPartsNum = splittedName.size();
                wikiDataName = splittedName.get(splitPartsNum - 1);
                wikiDataNamesList.add(wikiDataName);
            }
        }
        Logger.d("Found " + wikiDataNamesList.size() + " wikiNames in " + ecodeObjects.size() + " additives");
        return wikiDataNamesList;
    }

    public static ArrayList<String> matchEcodes(String recognizedText, List<String> eCodesList) {
        ArrayList<String> matchedEcodeList = new ArrayList<>();

        // check recognized text for each additive ecode
        for (int i = 0; i < mAdditivesNum; i++) {
            // "E322i"
            String eCode = eCodesList.get(i);
            if (recognizedText.contains(eCode)) {
                matchedEcodeList.add(eCode);
            }
        }

        return matchedEcodeList;
    }

    public static String stripNonDigits(final CharSequence input) {
        final StringBuilder sb = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            final char c = input.charAt(i);
            if (c > 47 && c < 58) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static boolean checkListNullEmpty(ArrayList<String> arrayList) {
        return arrayList != null && !arrayList.isEmpty();
    }

    public static boolean imageTypeSupported(String imageUrl) {
        String imageType = imageUrl.substring(imageUrl.length() - 3, imageUrl.length());
        return !imageType.equals("svg") && !imageType.equals("tif");
    }
}
