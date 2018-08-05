package com.github.neone35.enalyzer.models.remotejson.pubchemhazards;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class ReferenceItem {

    @SerializedName("ReferenceNumber")
    private int referenceNumber;

    @SerializedName("SourceID")
    private String sourceID;

    @SerializedName("SourceName")
    private String sourceName;

    @SerializedName("URL")
    private String uRL;

    @SerializedName("Name")
    private String name;

    @SerializedName("Description")
    private String description;

    public void setReferenceNumber(int referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public int getReferenceNumber() {
        return referenceNumber;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setURL(String uRL) {
        this.uRL = uRL;
    }

    public String getURL() {
        return uRL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return
                "ReferenceItem{" +
                        "referenceNumber = '" + referenceNumber + '\'' +
                        ",sourceID = '" + sourceID + '\'' +
                        ",sourceName = '" + sourceName + '\'' +
                        ",uRL = '" + uRL + '\'' +
                        ",name = '" + name + '\'' +
                        ",description = '" + description + '\'' +
                        "}";
    }
}