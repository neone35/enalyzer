package com.github.neone35.enalyzer.models.remotegson.pubchemhazards;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class SectionItem {

    @SerializedName("Description")
    private String description;

    @SerializedName("TOCHeading")
    private String tOCHeading;

    @SerializedName("Section")
    private List<SectionItem> section;

    @SerializedName("Information")
    private List<InformationItem> information;

    @SerializedName("HintShowAtMost")
    private int hintShowAtMost;

    @SerializedName("HintSortByLength")
    private boolean hintSortByLength;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTOCHeading(String tOCHeading) {
        this.tOCHeading = tOCHeading;
    }

    public String getTOCHeading() {
        return tOCHeading;
    }

    public void setSection(List<SectionItem> section) {
        this.section = section;
    }

    public List<SectionItem> getSection() {
        return section;
    }

    public void setInformation(List<InformationItem> information) {
        this.information = information;
    }

    public List<InformationItem> getInformation() {
        return information;
    }

    public void setHintShowAtMost(int hintShowAtMost) {
        this.hintShowAtMost = hintShowAtMost;
    }

    public int getHintShowAtMost() {
        return hintShowAtMost;
    }

    public void setHintSortByLength(boolean hintSortByLength) {
        this.hintSortByLength = hintSortByLength;
    }

    public boolean isHintSortByLength() {
        return hintSortByLength;
    }

    @Override
    public String toString() {
        return
                "SectionItem{" +
                        "description = '" + description + '\'' +
                        ",tOCHeading = '" + tOCHeading + '\'' +
                        ",section = '" + section + '\'' +
                        ",information = '" + information + '\'' +
                        ",hintShowAtMost = '" + hintShowAtMost + '\'' +
                        ",hintSortByLength = '" + hintSortByLength + '\'' +
                        "}";
    }
}