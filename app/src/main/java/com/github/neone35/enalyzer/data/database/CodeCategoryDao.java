package com.github.neone35.enalyzer.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.github.neone35.enalyzer.data.models.room.CodeCategory;

import java.util.List;

@Dao
public interface CodeCategoryDao {
    @Query("SELECT * FROM code_categories")
    LiveData<List<CodeCategory>> getAll();

    @Query("SELECT * FROM code_categories where id = :id")
    LiveData<CodeCategory> getById(int id);

    @Query("SELECT e_codes FROM code_categories where id = :id ORDER BY codes ASC")
    List<String> getCategoryEcodesStaticById(int id);

    @Query("SELECT * FROM code_categories WHERE codes BETWEEN 100 AND 199")
    LiveData<CodeCategory> loadColours();

    @Query("SELECT * FROM code_categories WHERE codes BETWEEN 200 AND 299")
    LiveData<CodeCategory> loadPreservatives();

    @Query("SELECT * FROM code_categories WHERE codes BETWEEN 300 AND 399")
    LiveData<CodeCategory> loadAntioxidants();

    @Query("SELECT * FROM code_categories WHERE codes BETWEEN 400 AND 499")
    LiveData<CodeCategory> loadThickeners();

    @Query("SELECT * FROM code_categories WHERE codes BETWEEN 500 AND 599")
    LiveData<CodeCategory> loadAcidityRegulators();

    @Query("SELECT * FROM code_categories WHERE codes BETWEEN 600 AND 699")
    LiveData<CodeCategory> loadFlavourEnhancers();

    @Query("SELECT * FROM code_categories WHERE codes BETWEEN 700 AND 799")
    LiveData<CodeCategory> loadAntibiotics();

    @Query("SELECT * FROM code_categories WHERE codes BETWEEN 900 AND 999")
    LiveData<CodeCategory> loadGlazingAgents();

    @Query("SELECT * FROM code_categories WHERE codes BETWEEN 1000 AND 1599")
    LiveData<CodeCategory> loadAdditional();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(List<CodeCategory> codeCategories);

    @Update
    void bulkUpdate(List<CodeCategory> codeCategories);

    @Query("DELETE FROM code_categories")
    void deleteAllCodeCategories();
}
