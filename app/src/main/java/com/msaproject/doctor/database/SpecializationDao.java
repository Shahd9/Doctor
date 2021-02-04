package com.msaproject.doctor.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.msaproject.doctor.model.SpecializationModel;

import java.util.List;

@Dao
public interface SpecializationDao {
    @Query("SELECT * FROM SpecializationModel ORDER BY name ASC")
    LiveData<List<SpecializationModel>> getAllSpecialization();

    @Query("SELECT * FROM SpecializationModel WHERE id = :id LIMIT 1")
    LiveData<SpecializationModel> getSpecializationById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUpdatedSpecializations(List<SpecializationModel> specializationModels);
}
