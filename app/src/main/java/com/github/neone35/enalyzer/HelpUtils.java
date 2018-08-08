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
    public static ArrayList<String> mExtractedEcodes = new ArrayList<String>();
    public static ArrayList<String> mFoundEcodes = new ArrayList<String>();

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

    public static List<EcodeListItem> getLocalEcodesList(String additivesJsonString) {
        Type responseType = new TypeToken<EcodeListResponse>() {
        }.getType();
        EcodeListResponse ecodeListResponse = gson.fromJson(additivesJsonString, responseType);
        mAdditivesNum = ecodeListResponse.getCount();
        String ecodeItemListString = gson.toJson(ecodeListResponse.getEcodeList());
        Type itemListType = new TypeToken<List<EcodeListItem>>() {
        }.getType();
        return gson.fromJson(ecodeItemListString, itemListType);
    }

    public static ArrayList<String> extractEcodes(List<EcodeListItem> ecodeListItems) {
        for (int i = 0; i < HelpUtils.mAdditivesNum; i++) {
            // "en:e322i"
            List<String> splittedId = Splitter.onPattern(":").splitToList(ecodeListItems.get(i).getId());
            // "e322i"
            String eCode = splittedId.get(1);
            // "E322i"
            String eCodeCapitalE = eCode.substring(0, 1).toUpperCase() + eCode.substring(1);
            mExtractedEcodes.add(eCodeCapitalE);
        }
        return mExtractedEcodes;
    }

    public static ArrayList<String> findEcodes(List<String> extractedEcodes) {
        int extractedNum = extractedEcodes.size();
        for (int i = 0; i < extractedNum; i++) {

        }
        return null;
    }
}
