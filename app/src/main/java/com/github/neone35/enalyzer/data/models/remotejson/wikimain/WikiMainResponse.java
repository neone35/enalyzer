package com.github.neone35.enalyzer.data.models.remotejson.wikimain;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class WikiMainResponse {

    @SerializedName("batchcomplete")
    private String batchcomplete;

    @SerializedName("query")
    private WikiMainQuery wikiMainQuery;

    public void setBatchcomplete(String batchcomplete) {
        this.batchcomplete = batchcomplete;
    }

    public String getBatchcomplete() {
        return batchcomplete;
    }

    public void setWikiMainQuery(WikiMainQuery wikiMainQuery) {
        this.wikiMainQuery = wikiMainQuery;
    }

    public WikiMainQuery getWikiMainQuery() {
        return wikiMainQuery;
    }

    @Override
    public String toString() {
        return
                "WikiMainResponse{" +
                        "batchcomplete = '" + batchcomplete + '\'' +
                        ",wikiMainQuery = '" + wikiMainQuery + '\'' +
                        "}";
    }
}