package com.github.neone35.enalyzer.models.remotegson.wikidatatitle;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class WikiDataEntity {

    @SerializedName("aspects")
    private List<String> aspects;

    @SerializedName("url")
    private String url;

    public void setAspects(List<String> aspects) {
        this.aspects = aspects;
    }

    public List<String> getAspects() {
        return aspects;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return
                "WikiDataEntity{" +
                        "aspects = '" + aspects + '\'' +
                        ",url = '" + url + '\'' +
                        "}";
    }
}