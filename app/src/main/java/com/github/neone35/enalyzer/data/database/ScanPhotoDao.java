package com.github.neone35.enalyzer.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.github.neone35.enalyzer.data.models.room.Additive;
import com.github.neone35.enalyzer.data.models.room.ScanPhoto;

import java.util.List;

@Dao
public interface ScanPhotoDao {
    @Query("SELECT * FROM scan_photos")
    LiveData<List<ScanPhoto>> getAll();

    @Query("SELECT * FROM scan_photos where id = :id")
    LiveData<ScanPhoto> getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOne(ScanPhoto scanPhoto);

    @Query("DELETE FROM scan_photos")
    void deleteAllScanPhotos();

    @Query("DELETE FROM scan_photos where id IN (:ids)")
    void deleteById(Integer... ids);
}
