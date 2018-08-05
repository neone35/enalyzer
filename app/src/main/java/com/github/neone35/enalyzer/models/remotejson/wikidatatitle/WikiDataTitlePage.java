package com.github.neone35.enalyzer.models.remotejson.wikidatatitle;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

@Generated("com.robohorse.robopojogenerator")
public class WikiDataTitlePage {

    @SerializedName("ns")
    private int ns;

    @SerializedName("title")
    private String title;

    @SerializedName("pageid")
    private int pageid;

    @SerializedName("wblistentityusage")
    private HashMap<String, WikiDataEntity> wikiDataEntity;

    public void setNs(int ns) {
        this.ns = ns;
    }

    public int getNs() {
        return ns;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public int getPageid() {
        return pageid;
    }

    public void setWikiDataEntity(HashMap<String, WikiDataEntity> wikiDataEntity) {
        this.wikiDataEntity = wikiDataEntity;
    }

    public HashMap<String, WikiDataEntity> getWikiDataEntity() {
        return wikiDataEntity;
    }

    @Override
    public String toString() {
        return
                "WikiDataTitlePage{" +
                        "ns = '" + ns + '\'' +
                        ",title = '" + title + '\'' +
                        ",pageid = '" + pageid + '\'' +
                        ",wikiDataEntity = '" + wikiDataEntity + '\'' +
                        "}";
    }
}