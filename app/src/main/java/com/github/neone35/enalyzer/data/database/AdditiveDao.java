package com.github.neone35.enalyzer.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.github.neone35.enalyzer.data.models.room.Additive;

import java.util.List;

@Dao
public interface AdditiveDao {
    @Query("SELECT * FROM additives")
    LiveData<List<Additive>> getAll();

    @Query("SELECT * FROM additives where ecode IN (:ecodes)")
    LiveData<List<Additive>> getBulkByEcode(List<String> ecodes);

    @Query("SELECT * FROM additives where ecode = :ecode")
    LiveData<Additive> getOneByEcode(String ecode);

    @Query("SELECT * FROM additives where ecode = :ecode")
    Additive getOneStaticByEcode(String ecode);

    @Query("SELECT * FROM additives where wiki_data_qcode = :qcode")
    Additive getOneStaticByQcode(String qcode);

    @Query("SELECT * FROM additives where name = :title")
    Additive getOneStaticByTitle(String title);

    @Query("SELECT * FROM additives where pubchem_ID = :CID")
    Additive getOneStaticByCID(int CID);

    @Query("SELECT * FROM additives where ecode IN (:ecodes)")
    List<Additive> getBulkStaticByEcode(List<String> ecodes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(List<Additive> additives);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void oneInsert(Additive additive);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void bulkUpdate(List<Additive> additives);

    @Query("DELETE FROM additives")
    void deleteAllAdditives();
}
