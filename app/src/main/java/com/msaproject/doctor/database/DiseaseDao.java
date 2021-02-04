package com.msaproject.doctor.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.msaproject.doctor.model.DiseaseModel;

import java.util.List;

@Dao
public interface DiseaseDao {
    @Query("SELECT * FROM DiseaseModel ORDER BY name ASC")
    LiveData<List<DiseaseModel>> getAllDiseases();

    @Query("SELECT * FROM DiseaseModel WHERE id = :id LIMIT 1")
    LiveData<DiseaseModel> getDiseaseById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUpdatedDiseases(List<DiseaseModel> diseaseModels);
}
