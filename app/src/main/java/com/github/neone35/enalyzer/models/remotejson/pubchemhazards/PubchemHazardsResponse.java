package com.github.neone35.enalyzer.models.remotejson.pubchemhazards;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class PubchemHazardsResponse {

    @SerializedName("Record")
    private Record record;

    public void setRecord(Record record) {
        this.record = record;
    }

    public Record getRecord() {
        return record;
    }

    @Override
    public String toString() {
        return
                "PubchemHazardsResponse{" +
                        "record = '" + record + '\'' +
                        "}";
    }
}