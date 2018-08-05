package com.github.neone35.enalyzer.models.local;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class ClassificationResponse {

    @SerializedName("code")
    private String code;

    @SerializedName("signal word")
    private String signalWord;

    @SerializedName("storage")
    private String storage;

    @SerializedName("pictogram")
    private String pictogram;

    @SerializedName("hazard statements")
    private String hazardStatements;

    @SerializedName("prevention ")
    private String prevention;

    @SerializedName("hazard class")
    private String hazardClass;

    @SerializedName("category")
    private String category;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setSignalWord(String signalWord) {
        this.signalWord = signalWord;
    }

    public String getSignalWord() {
        return signalWord;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getStorage() {
        return storage;
    }

    public void setPictogram(String pictogram) {
        this.pictogram = pictogram;
    }

    public String getPictogram() {
        return pictogram;
    }

    public void setHazardStatements(String hazardStatements) {
        this.hazardStatements = hazardStatements;
    }

    public String getHazardStatements() {
        return hazardStatements;
    }

    public void setPrevention(String prevention) {
        this.prevention = prevention;
    }

    public String getPrevention() {
        return prevention;
    }

    public void setHazardClass(String hazardClass) {
        this.hazardClass = hazardClass;
    }

    public String getHazardClass() {
        return hazardClass;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return
                "ClassificationResponse{" +
                        "code = '" + code + '\'' +
                        ",signal word = '" + signalWord + '\'' +
                        ",storage = '" + storage + '\'' +
                        ",pictogram = '" + pictogram + '\'' +
                        ",hazard statements = '" + hazardStatements + '\'' +
                        ",prevention  = '" + prevention + '\'' +
                        ",hazard class = '" + hazardClass + '\'' +
                        ",category = '" + category + '\'' +
                        "}";
    }
}