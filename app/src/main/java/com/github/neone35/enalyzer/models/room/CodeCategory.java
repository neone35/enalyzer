package com.github.neone35.enalyzer.models.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "code_categories")
public class CodeCategory {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String range;
    private String title;
    @ColumnInfo(name = "e_codes")
    private List<String> Ecodes;

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

    public List<String> getEcodes() {
        return Ecodes;
    }

    public void setEcodes(List<String> ecodes) {
        Ecodes = ecodes;
    }
}
