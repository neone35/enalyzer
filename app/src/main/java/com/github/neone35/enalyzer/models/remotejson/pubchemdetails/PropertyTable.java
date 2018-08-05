package com.github.neone35.enalyzer.models.remotejson.pubchemdetails;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class PropertyTable {

    @SerializedName("Properties")
    private List<PropertiesItem> properties;

    public void setProperties(List<PropertiesItem> properties) {
        this.properties = properties;
    }

    public List<PropertiesItem> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return
                "PropertyTable{" +
                        "properties = '" + properties + '\'' +
                        "}";
    }
}