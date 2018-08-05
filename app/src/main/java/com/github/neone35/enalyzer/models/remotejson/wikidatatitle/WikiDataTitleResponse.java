package com.github.neone35.enalyzer.models.remotejson.wikidatatitle;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class WikiDataTitleResponse {

    @SerializedName("batchcomplete")
    private String batchcomplete;

    @SerializedName("query")
    private Query query;

    public void setBatchcomplete(String batchcomplete) {
        this.batchcomplete = batchcomplete;
    }

    public String getBatchcomplete() {
        return batchcomplete;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Query getQuery() {
        return query;
    }

    @Override
    public String toString() {
        return
                "WikiDataTitleResponse{" +
                        "batchcomplete = '" + batchcomplete + '\'' +
                        ",query = '" + query + '\'' +
                        "}";
    }
}