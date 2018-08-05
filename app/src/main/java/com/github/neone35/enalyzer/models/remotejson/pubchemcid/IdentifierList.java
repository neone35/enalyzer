package com.github.neone35.enalyzer.models.remotejson.pubchemcid;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class IdentifierList {

    @SerializedName("CID")
    private List<Integer> cID;

    public void setCID(List<Integer> cID) {
        this.cID = cID;
    }

    public List<Integer> getCID() {
        return cID;
    }

    @Override
    public String toString() {
        return
                "IdentifierList{" +
                        "cID = '" + cID + '\'' +
                        "}";
    }
}