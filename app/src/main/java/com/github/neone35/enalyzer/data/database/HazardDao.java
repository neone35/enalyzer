package com.github.neone35.enalyzer.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.github.neone35.enalyzer.data.models.room.Hazard;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface HazardDao {
    @Query("SELECT * FROM hazards")
    LiveData<List<Hazard>> getAll();

    @Query("SELECT * FROM hazards where statement_code IN (:statementCodes)")
    LiveData<List<Hazard>> getBulkByStatementCodes(List<String> statementCodes);

    @Query("SELECT * FROM hazards where statement_code = :statementCode")
    LiveData<Hazard> getOneByStatementCode(String statementCode);

    @Query("SELECT * FROM hazards")
    List<Hazard> getStaticAll();

    @Query("SELECT statement_code FROM hazards")
    List<String> getStaticAllStatementCodes();

    @Query("SELECT statement FROM hazards")
    List<String> getStaticAllStatements();

    @Query("SELECT * FROM hazards where statement_code = :statementCode")
    Hazard getOneStaticByStatementCode(String statementCode);

    @Query("SELECT * FROM hazards where statement_code IN (:statementCodes)")
    List<Hazard> getBulkStaticByStatementCodes(List<String> statementCodes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(List<Hazard> hazards);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void oneInsert(Hazard hazard);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void bulkUpdate(List<Hazard> hazards);

    @Query("DELETE FROM hazards")
    void deleteAllHazards();

}
