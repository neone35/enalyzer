package com.github.neone35.enalyzer.data.network;

import android.net.Uri;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.github.neone35.enalyzer.data.models.remotejson.wikidatatitle.WikiDataTitlePage;
import com.github.neone35.enalyzer.data.models.remotejson.wikidatatitle.WikiDataTitleResponse;
import com.google.common.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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

    public static List<String> getWikiTitleList(String... qCodes) {
        WikiEndpointInterface wikiEndpointInterface = getWikiApiService();
        List<String> additiveTitles = new ArrayList<>();
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
                    Logger.d("No response received for qCode " + qCode);
                }
            }
        } catch (IOException e) {
            Logger.d(e.getMessage());
        }
        return additiveTitles;
    }

    public static WikiEndpointInterface getWikiApiService() {
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

    public static PubchemEndpointInterface getPubchemApiService() {
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

    // imageFilename ex. Uhličitan_amonný.JPG
    public static String getWikiImgUrl(String imgFilename) {
        // 98e10a0a133d46ffef7de40065a4d758
        String MD5OfFilename = EncryptUtils.encryptMD5ToString(imgFilename);
        String MD5FirstChar = MD5OfFilename.substring(0, 1); // 9
        String MD5SecondChar = MD5OfFilename.substring(1, 2); // 8

        // https://upload.wikimedia.org/wikipedia/commons/9/98/
        String imgUrl = new Uri.Builder().scheme("https")
                .authority(WIKI_IMG_AUTHORITY)
                .appendPath("wikipedia")
                .appendPath("commons")
                .appendPath(MD5FirstChar)
                .appendPath(MD5FirstChar + MD5SecondChar)
                .build().toString();
        // https://upload.wikimedia.org/wikipedia/commons/9/98/Uhličitan_amonný.JPG
        return imgUrl + imgFilename;
    }

}
