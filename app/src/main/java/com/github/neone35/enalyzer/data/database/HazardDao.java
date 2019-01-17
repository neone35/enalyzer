package com.github.neone35.enalyzer.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.github.neone35.enalyzer.data.models.room.Hazard;

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
