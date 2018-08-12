package com.github.neone35.enalyzer.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.github.neone35.enalyzer.data.models.room.Additive;

import java.util.List;

@Dao
public interface AdditiveDao {
    @Query("SELECT * FROM additives")
    LiveData<List<Additive>> getAll();

    @Query("SELECT * FROM additives where ecode IN (:ecodes)")
    LiveData<List<Additive>> getBulkByEcode(String... ecodes);

    @Query("SELECT * FROM additives where ecode = :ecode")
    LiveData<Additive> getOneByEcode(String ecode);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(Additive[] additives);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void oneInsert(Additive additive);

    @Update
    void bulkUpdate(Additive[] additives);

    @Query("DELETE FROM additives")
    void deleteAllAdditives();
}
