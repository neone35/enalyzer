package com.github.neone35.enalyzer.data.models.remotejson.pubchemdetails;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class PubchemDetailsResponse {

    @SerializedName("PropertyTable")
    private PropertyTable propertyTable;

    public void setPropertyTable(PropertyTable propertyTable) {
        this.propertyTable = propertyTable;
    }

    public PropertyTable getPropertyTable() {
        return propertyTable;
    }

    @Override
    public String toString() {
        return
                "PubchemDetailsResponse{" +
                        "propertyTable = '" + propertyTable + '\'' +
                        "}";
    }
}