package com.github.neone35.enalyzer.models.remotegson.wikidatatitle;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

@Generated("com.robohorse.robopojogenerator")
public class Query {

    @SerializedName("pages")
    private HashMap<String, WikiDataTitlePage> wikiPage;

    public void setWikiPage(HashMap<String, WikiDataTitlePage> wikiPage) {
        this.wikiPage = wikiPage;
    }

    public HashMap<String, WikiDataTitlePage> getWikiPage() {
        return wikiPage;
    }

    @Override
    public String toString() {
        return
                "Query{" +
                        "wikiPage = '" + wikiPage + '\'' +
                        "}";
    }
}