package com.github.neone35.enalyzer.models.remotejson.pubchemdetails;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class PropertiesItem {

    @SerializedName("MolecularFormula")
    private String molecularFormula;

    @SerializedName("MolecularWeight")
    private double molecularWeight;

    @SerializedName("CID")
    private int cID;

    @SerializedName("Complexity")
    private int complexity;

    public void setMolecularFormula(String molecularFormula) {
        this.molecularFormula = molecularFormula;
    }

    public String getMolecularFormula() {
        return molecularFormula;
    }

    public void setMolecularWeight(double molecularWeight) {
        this.molecularWeight = molecularWeight;
    }

    public double getMolecularWeight() {
        return molecularWeight;
    }

    public void setCID(int cID) {
        this.cID = cID;
    }

    public int getCID() {
        return cID;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
    }

    public int getComplexity() {
        return complexity;
    }

    @Override
    public String toString() {
        return
                "PropertiesItem{" +
                        "molecularFormula = '" + molecularFormula + '\'' +
                        ",molecularWeight = '" + molecularWeight + '\'' +
                        ",cID = '" + cID + '\'' +
                        ",complexity = '" + complexity + '\'' +
                        "}";
    }
}