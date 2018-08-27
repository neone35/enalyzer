package com.github.neone35.enalyzer.data.models.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

@Entity(tableName = "additives")
public class Additive {

    @PrimaryKey
    @NonNull
    private String ecode;
    @ColumnInfo(name = "code")
    private int code;
    @ColumnInfo(name = "wiki_data_qcode")
    private String wikiDataQCode;
    @ColumnInfo(name = "wiki_edit_date")
    private String wikiEditDate;
    @ColumnInfo(name = "pubchem_ID")
    private int pubchemID;

    private String name;
    private String description;
    private String category;
    private String formula;
    private String imageURL;
    @ColumnInfo(name = "known_as")
    private List<String> knownAs;
    @ColumnInfo(name = "ghs_pictogram_urls")
    private List<String> ghsPictogramURL;
    @ColumnInfo(name = "hazards")
    private List<Hazard> hazardList;


    // Constructor used by Room to create Additives
    public Additive(@NonNull String ecode, int code, String wikiDataQCode, String wikiEditDate, int pubchemID,
                    String name, String description, String category, String formula, String imageURL,
                    List<String> knownAs, List<String> ghsPictogramURL, List<Hazard> hazardList) {
        this.ecode = ecode;
        this.code = code;
        this.wikiDataQCode = wikiDataQCode;
        this.wikiEditDate = wikiEditDate;
        this.pubchemID = pubchemID;
        this.name = name;
        this.description = description;
        this.category = category;
        this.formula = formula;
        this.imageURL = imageURL;
        this.knownAs = knownAs;
        this.ghsPictogramURL = ghsPictogramURL;
        this.hazardList = hazardList;
    }

    @NonNull
    public String getEcode() {
        return ecode;
    }

    public void setEcode(@NonNull String ecode) {
        this.ecode = ecode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getWikiDataQCode() {
        return wikiDataQCode;
    }

    public void setWikiDataQCode(String wikiDataQCode) {
        this.wikiDataQCode = wikiDataQCode;
    }

    public String getWikiEditDate() {
        return wikiEditDate;
    }

    public void setWikiEditDate(String wikiEditDate) {
        this.wikiEditDate = wikiEditDate;
    }

    public int getPubchemID() {
        return pubchemID;
    }

    public void setPubchemID(int pubchemID) {
        this.pubchemID = pubchemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public List<String> getKnownAs() {
        return knownAs;
    }

    public void setKnownAs(List<String> knownAs) {
        this.knownAs = knownAs;
    }

    public List<String> getGhsPictogramURL() {
        return ghsPictogramURL;
    }

    public void setGhsPictogramURL(List<String> ghsPictogramURL) {
        this.ghsPictogramURL = ghsPictogramURL;
    }

    public List<Hazard> getHazardList() {
        return hazardList;
    }

    public void setHazardList(List<Hazard> hazardList) {
        this.hazardList = hazardList;
    }
}
