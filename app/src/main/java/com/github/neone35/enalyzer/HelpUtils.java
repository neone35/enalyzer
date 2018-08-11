package com.github.neone35.enalyzer;

import android.content.Context;

import com.github.neone35.enalyzer.data.models.localjson.ecodelist.EcodeListItem;
import com.github.neone35.enalyzer.data.models.localjson.ecodelist.EcodeListResponse;
import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HelpUtils {

    private static Gson gson = new Gson();
    public static int mAdditivesNum = 0;

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
        return eCodeList;
    }

    public static ArrayList<String> getWikiDataQCodes(List<EcodeListItem> ecodeObjects) {
        ArrayList<String> wikiDataQCodeList = new ArrayList<>();
        for (int i = 0; i < mAdditivesNum; i++) {
            String wikiDataURL = ecodeObjects.get(i).getSameAs().get(0);
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
        }
        return wikiDataQCodeList;
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

    public static boolean checkListNullEmpty(ArrayList<String> arrayList) {
        return arrayList != null && !arrayList.isEmpty();
    }
}
