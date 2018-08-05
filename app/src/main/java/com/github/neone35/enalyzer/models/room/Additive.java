package com.github.neone35.enalyzer.models.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "additives")
public class Additive {

    @PrimaryKey
    private String Ecode;
    @ColumnInfo(name = "wiki_data_Id")
    private String wikiDataID;
    @ColumnInfo(name = "wiki_edit_date")
    private String wikiEditDate;
    @ColumnInfo(name = "pubchem_ID")
    private String pubchemID;

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

}
