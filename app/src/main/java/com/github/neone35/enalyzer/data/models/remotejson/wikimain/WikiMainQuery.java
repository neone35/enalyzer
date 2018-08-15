package com.github.neone35.enalyzer.data.models.remotejson.wikimain;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

@Generated("com.robohorse.robopojogenerator")
public class WikiMainQuery {

    @SerializedName("pages")
    private HashMap<String, WikiMainPage> wikiPageMap;

    public void setWikiPageMap(HashMap<String, WikiMainPage> wikiPageMap) {
        this.wikiPageMap = this.wikiPageMap;
    }

    public HashMap<String, WikiMainPage> getWikiPageMap() {
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