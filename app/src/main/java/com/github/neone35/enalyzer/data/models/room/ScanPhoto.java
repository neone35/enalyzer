package com.github.neone35.enalyzer.data.models.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "scan_photos")
public class ScanPhoto {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "file_name")
    private String fileName;
    private String date;
    @ColumnInfo(name = "e_codes")
    private List<String> ECodes;

    // Constructor used by Room to create Additives
    public ScanPhoto(int id, String fileName, String date, List<String> ECodes) {
        this.id = id;
        this.fileName = fileName;
        this.date = date;
        this.ECodes = ECodes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getECodes() {
        return ECodes;
    }

    public void setECodes(List<String> ECodes) {
        this.ECodes = ECodes;
    }
}
