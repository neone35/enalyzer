package com.github.neone35.enalyzer.data.models.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "additives")
public class Additive {

    @PrimaryKey
    private int ecode;
    @ColumnInfo(name = "wiki_data_Id")
    private String wikiDataID;
    @ColumnInfo(name = "wiki_edit_date")
    private String wikiEditDate;
    @ColumnInfo(name = "pubchem_ID")
    private String pubchemID;

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
    private List<Hazard> hazard;

    // Constructor used by Room to create Additives
    public Additive(int ecode, String wikiDataID, String wikiEditDate, String pubchemID,
                    String name, String description, String category, String formula, String imageURL,
                    List<String> knownAs, List<String> ghsPictogramURL, List<Hazard> hazard) {
        this.ecode = ecode;
        this.wikiDataID = wikiDataID;
        this.wikiEditDate = wikiEditDate;
        this.pubchemID = pubchemID;
        this.name = name;
        this.description = description;
        this.category = category;
        this.formula = formula;
        this.imageURL = imageURL;
        this.knownAs = knownAs;
        this.ghsPictogramURL = ghsPictogramURL;
        this.hazard = hazard;
    }

    public int getEcode() {
        return ecode;
    }

    public String getWikiDataID() {
        return wikiDataID;
    }

    public String getWikiEditDate() {
        return wikiEditDate;
    }

    public String getPubchemID() {
        return pubchemID;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getFormula() {
        return formula;
    }

    public String getImageURL() {
        return imageURL;
    }

    public List<String> getKnownAs() {
        return knownAs;
    }

    public List<String> getGhsPictogramURL() {
        return ghsPictogramURL;
    }

    public List<Hazard> getHazard() {
        return hazard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
