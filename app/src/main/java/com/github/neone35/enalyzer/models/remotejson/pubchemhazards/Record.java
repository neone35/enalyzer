package com.github.neone35.enalyzer.models.remotejson.pubchemhazards;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Record {

    @SerializedName("RecordType")
    private String recordType;

    @SerializedName("Reference")
    private List<ReferenceItem> reference;

    @SerializedName("RecordNumber")
    private int recordNumber;

    @SerializedName("Section")
    private List<SectionItem> section;

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setReference(List<ReferenceItem> reference) {
        this.reference = reference;
    }

    public List<ReferenceItem> getReference() {
        return reference;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setSection(List<SectionItem> section) {
        this.section = section;
    }

    public List<SectionItem> getSection() {
        return section;
    }

    @Override
    public String toString() {
        return
                "Record{" +
                        "recordType = '" + recordType + '\'' +
                        ",reference = '" + reference + '\'' +
                        ",recordNumber = '" + recordNumber + '\'' +
                        ",section = '" + section + '\'' +
                        "}";
    }
}