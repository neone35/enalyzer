package com.github.neone35.enalyzer.data.models.remotejson.wikimain;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

@Generated("com.robohorse.robopojogenerator")
public class Query {

    @SerializedName("pages")
    private HashMap<String, WikiMainPage> wikiPage;

    public void setWikiPage(HashMap<String, WikiMainPage> wikiPage) {
        this.wikiPage = this.wikiPage;
    }

    public HashMap<String, WikiMainPage> getWikiPage() {
        return wikiPage;
    }

    @Override
    public String toString() {
        return
                "WikiDataQuery{" +
                        "wikiPage = '" + wikiPage + '\'' +
                        "}";
    }
}