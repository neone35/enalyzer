package com.github.neone35.enalyzer.data.models.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "scan_photos")
public class ScanPhoto {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "file_name")
    private String filePath;
    @ColumnInfo(name = "time_millis")
    private long timeMillis;
    @ColumnInfo(name = "e_codes")
    private List<String> ECodes;

    // Constructor used by Room to create Additives
    public ScanPhoto(String filePath, long timeMillis, List<String> ECodes) {
        this.filePath = filePath;
        this.timeMillis = timeMillis;
        this.ECodes = ECodes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public void setTimeMillis(long date) {
        this.timeMillis = date;
    }

    public List<String> getECodes() {
        return ECodes;
    }

    public void setECodes(List<String> ECodes) {
        this.ECodes = ECodes;
    }
}
