package com.github.neone35.enalyzer.models.remotegson.wikimain;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class WikiMainPage {

    @SerializedName("touched")
    private String touched;

    @SerializedName("ns")
    private int ns;

    @SerializedName("contentmodel")
    private String contentmodel;

    @SerializedName("pagelanguagehtmlcode")
    private String pagelanguagehtmlcode;

    @SerializedName("length")
    private int length;

    @SerializedName("pageid")
    private int pageid;

    @SerializedName("title")
    private String title;

    @SerializedName("lastrevid")
    private int lastrevid;

    @SerializedName("extract")
    private String extract;

    @SerializedName("terms")
    private WikiMainPageTerms wikiMainPageTerms;

    @SerializedName("pagelanguage")
    private String pagelanguage;

    @SerializedName("pagelanguagedir")
    private String pagelanguagedir;

    @SerializedName("pageprops")
    private WikiMainPageProps wikiMainPageProps;

    public void setTouched(String touched) {
        this.touched = touched;
    }

    public String getTouched() {
        return touched;
    }

    public void setNs(int ns) {
        this.ns = ns;
    }

    public int getNs() {
        return ns;
    }

    public void setContentmodel(String contentmodel) {
        this.contentmodel = contentmodel;
    }

    public String getContentmodel() {
        return contentmodel;
    }

    public void setPagelanguagehtmlcode(String pagelanguagehtmlcode) {
        this.pagelanguagehtmlcode = pagelanguagehtmlcode;
    }

    public String getPagelanguagehtmlcode() {
        return pagelanguagehtmlcode;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public int getPageid() {
        return pageid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setLastrevid(int lastrevid) {
        this.lastrevid = lastrevid;
    }

    public int getLastrevid() {
        return lastrevid;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    public String getExtract() {
        return extract;
    }

    public void setWikiMainPageTerms(WikiMainPageTerms wikiMainPageTerms) {
        this.wikiMainPageTerms = wikiMainPageTerms;
    }

    public WikiMainPageTerms getWikiMainPageTerms() {
        return wikiMainPageTerms;
    }

    public void setPagelanguage(String pagelanguage) {
        this.pagelanguage = pagelanguage;
    }

    public String getPagelanguage() {
        return pagelanguage;
    }

    public void setPagelanguagedir(String pagelanguagedir) {
        this.pagelanguagedir = pagelanguagedir;
    }

    public String getPagelanguagedir() {
        return pagelanguagedir;
    }

    public void setWikiMainPageProps(WikiMainPageProps wikiMainPageProps) {
        this.wikiMainPageProps = wikiMainPageProps;
    }

    public WikiMainPageProps getWikiMainPageProps() {
        return wikiMainPageProps;
    }

    @Override
    public String toString() {
        return
                "WikiDataTitlePage{" +
                        "touched = '" + touched + '\'' +
                        ",ns = '" + ns + '\'' +
                        ",contentmodel = '" + contentmodel + '\'' +
                        ",pagelanguagehtmlcode = '" + pagelanguagehtmlcode + '\'' +
                        ",length = '" + length + '\'' +
                        ",pageid = '" + pageid + '\'' +
                        ",title = '" + title + '\'' +
                        ",lastrevid = '" + lastrevid + '\'' +
                        ",extract = '" + extract + '\'' +
                        ",wikiMainPageTerms = '" + wikiMainPageTerms + '\'' +
                        ",pagelanguage = '" + pagelanguage + '\'' +
                        ",pagelanguagedir = '" + pagelanguagedir + '\'' +
                        ",wikiMainPageProps = '" + wikiMainPageProps + '\'' +
                        "}";
    }
}