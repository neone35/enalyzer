package com.github.neone35.enalyzer.models.remotejson.pubchemcid;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class PubchemCIDResponse {

    @SerializedName("IdentifierList")
    private IdentifierList identifierList;

    public void setIdentifierList(IdentifierList identifierList) {
        this.identifierList = identifierList;
    }

    public IdentifierList getIdentifierList() {
        return identifierList;
    }

    @Override
    public String toString() {
        return
                "PubchemCIDResponse{" +
                        "identifierList = '" + identifierList + '\'' +
                        "}";
    }
}