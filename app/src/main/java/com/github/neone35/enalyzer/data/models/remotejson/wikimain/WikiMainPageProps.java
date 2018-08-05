package com.github.neone35.enalyzer.data.models.remotejson.wikimain;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class WikiMainPageProps {

    @SerializedName("wikibase_item")
    private String wikibaseItem;

    @SerializedName("page_image_free")
    private String pageImageFree;

    public void setWikibaseItem(String wikibaseItem) {
        this.wikibaseItem = wikibaseItem;
    }

    public String getWikibaseItem() {
        return wikibaseItem;
    }

    public void setPageImageFree(String pageImageFree) {
        this.pageImageFree = pageImageFree;
    }

    public String getPageImageFree() {
        return pageImageFree;
    }

    @Override
    public String toString() {
        return
                "WikiMainPageProps{" +
                        "wikibase_item = '" + wikibaseItem + '\'' +
                        ",page_image_free = '" + pageImageFree + '\'' +
                        "}";
    }
}