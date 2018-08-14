package com.github.neone35.enalyzer.data.models.remotejson.wikidatatitle;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class WikiDataTitleResponse {

    @SerializedName("batchcomplete")
    private String batchcomplete;

    @SerializedName("query")
    private WikiDataQuery wikiDataQuery;

    public void setBatchcomplete(String batchcomplete) {
        this.batchcomplete = batchcomplete;
    }

    public String getBatchcomplete() {
        return batchcomplete;
    }

    public void setWikiDataQuery(WikiDataQuery wikiDataQuery) {
        this.wikiDataQuery = wikiDataQuery;
    }

    public WikiDataQuery getWikiDataQuery() {
        return wikiDataQuery;
    }

    @Override
    public String toString() {
        return
                "WikiDataTitleResponse{" +
                        "batchcomplete = '" + batchcomplete + '\'' +
                        ",wikiDataQuery = '" + wikiDataQuery + '\'' +
                        "}";
    }
}