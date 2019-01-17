package com.github.neone35.enalyzer.data.models.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "code_categories")
public class CodeCategory {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String range;
    private String title;
    private List<Integer> codes;
    @ColumnInfo(name = "e_codes")
    private List<String> ECodes;

    // Constructor used by Room to create CodeCategories
    public CodeCategory(String range, String title, List<Integer> codes, List<String> ECodes) {
        this.range = range;
        this.title = title;
        this.codes = codes;
        this.ECodes = ECodes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getCodes() {
        return codes;
    }

    public void setCodes(List<Integer> ECodes) {
        this.codes = ECodes;
    }

    public List<String> getECodes() {
        return ECodes;
    }

    public void setECodes(List<String> ECodes) {
        this.ECodes = ECodes;
    }
}
