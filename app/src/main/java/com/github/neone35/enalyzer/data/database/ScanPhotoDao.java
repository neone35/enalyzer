package com.github.neone35.enalyzer.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.github.neone35.enalyzer.data.models.room.ScanPhoto;

import java.util.List;

@Dao
public interface ScanPhotoDao {
    @Query("SELECT * FROM scan_photos ORDER BY time_millis DESC")
    LiveData<List<ScanPhoto>> getAll();

    @Query("SELECT * FROM scan_photos where id = :id")
    LiveData<ScanPhoto> getById(int id);

    @Query("SELECT * FROM scan_photos where id = :id")
    ScanPhoto getOneStaticById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOne(ScanPhoto scanPhoto);

    @Query("DELETE FROM scan_photos")
    void deleteAllScanPhotos();

    @Query("DELETE FROM scan_photos where id IN (:ids)")
    void deleteById(List<Integer> ids);
}
