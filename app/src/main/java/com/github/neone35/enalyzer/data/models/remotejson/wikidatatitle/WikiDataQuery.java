package com.github.neone35.enalyzer.data.models.remotejson.wikidatatitle;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

@Generated("com.robohorse.robopojogenerator")
public class WikiDataQuery {

    @SerializedName("pages")
    private HashMap<String, WikiDataTitlePage> wikiPageMap;

    public void setWikiPageMap(HashMap<String, WikiDataTitlePage> wikiPageMap) {
        this.wikiPageMap = wikiPageMap;
    }

    public HashMap<String, WikiDataTitlePage> getWikiPageMap() {
        return wikiPageMap;
    }

    @Override
    public String toString() {
        return
                "WikiDataQuery{" +
                        "wikiPageMap = '" + wikiPageMap + '\'' +
                        "}";
    }
}