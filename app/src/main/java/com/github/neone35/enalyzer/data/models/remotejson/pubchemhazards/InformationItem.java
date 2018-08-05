package com.github.neone35.enalyzer.data.models.remotejson.pubchemhazards;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class InformationItem {

    @SerializedName("Description")
    private String description;

    @SerializedName("Reference")
    private List<String> reference;

    @SerializedName("ReferenceNumber")
    private int referenceNumber;

    @SerializedName("StringValue")
    private String stringValue;

    @SerializedName("Name")
    private String name;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setReference(List<String> reference) {
        this.reference = reference;
    }

    public List<String> getReference() {
        return reference;
    }

    public void setReferenceNumber(int referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public int getReferenceNumber() {
        return referenceNumber;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return
                "InformationItem{" +
                        "description = '" + description + '\'' +
                        ",reference = '" + reference + '\'' +
                        ",referenceNumber = '" + referenceNumber + '\'' +
                        ",stringValue = '" + stringValue + '\'' +
                        ",name = '" + name + '\'' +
                        "}";
    }
}