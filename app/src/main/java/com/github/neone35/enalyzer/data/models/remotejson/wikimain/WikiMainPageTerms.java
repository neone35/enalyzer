package com.github.neone35.enalyzer.data.models.remotejson.wikimain;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class WikiMainPageTerms {

    @SerializedName("description")
    private List<String> description;

    @SerializedName("label")
    private List<String> label;

    @SerializedName("alias")
    private List<String> alias;

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setLabel(List<String> label) {
        this.label = label;
    }

    public List<String> getLabel() {
        return label;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    public List<String> getAlias() {
        return alias;
    }

    @Override
    public String toString() {
        return
                "WikiMainPageTerms{" +
                        "alias = '" + alias + '\'' +
                        ",description = '" + description + '\'' +
                        ",label = '" + label + '\'' +
                        "}";
    }
}