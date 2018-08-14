package com.github.neone35.enalyzer.data.network;

import com.github.neone35.enalyzer.data.models.remotejson.wikidatatitle.WikiDataTitleResponse;
import com.github.neone35.enalyzer.data.models.remotejson.wikimain.WikiMainResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WikiEndpointInterface {

    String actionQuery = "/w/api.php?format=json&action=query";

    @GET(actionQuery + "&list=wblistentityusage&wbeuprop=url&wbeulimit=1")
    Call<WikiDataTitleResponse> getAdditiveTitleByWikiQCode(@Query("wbeuentities") String qCode);

    @GET(actionQuery + "&prop=extracts|pageprops|pageterms|info&exintro=&explaintext=&redirects=1")
    Call<WikiMainResponse> getWikiMainByAdditiveTitle(@Query("titles") String... titles);
}
