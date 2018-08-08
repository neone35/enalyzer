package com.github.neone35.enalyzer.data.models.localjson.ecodelist;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class EcodeListResponse {

    @SerializedName("count")
    private int count;

    @SerializedName("tags")
    private List<EcodeListItem> ecodeList;

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setEcodeList(List<EcodeListItem> ecodeList) {
        this.ecodeList = ecodeList;
    }

    public List<EcodeListItem> getEcodeList() {
        return ecodeList;
    }

    @Override
    public String toString() {
        return
                "EcodeListResponse{" +
                        "count = '" + count + '\'' +
                        ",ecodeList = '" + ecodeList + '\'' +
                        "}";
    }
}