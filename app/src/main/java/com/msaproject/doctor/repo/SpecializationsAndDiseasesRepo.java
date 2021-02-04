package com.msaproject.doctor.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.msaproject.doctor.base.BaseRepo;
import com.msaproject.doctor.database.AppDatabase;
import com.msaproject.doctor.database.DiseaseDao;
import com.msaproject.doctor.database.SpecializationDao;
import com.msaproject.doctor.model.DiseaseModel;
import com.msaproject.doctor.model.SpecializationModel;
import com.msaproject.doctor.network.Endpoints;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class SpecializationsAndDiseasesRepo extends BaseRepo {

    @Inject
    public SpecializationsAndDiseasesRepo() {
    }

    public LiveData<Boolean> updateSpecializationsAndDiseases() {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        SpecializationDao specializationDao = AppDatabase.getInstance().getSpecializationDao();
        DiseaseDao diseaseDao = AppDatabase.getInstance().getDiseaseDao();
        Completable completable =
                firebaseManager.getDocumentsInCollection(Endpoints.COLLECTION_SPECIALIZATIONS, SpecializationModel.class)
                        .flatMap(specializationModels ->
                                firebaseManager.getDocumentsInCollection(Endpoints.COLLECTION_DISEASES, DiseaseModel.class)
                                        .map(diseaseModels -> {
                                            specializationDao.insertUpdatedSpecializations(specializationModels);
                                            diseaseDao.insertUpdatedDiseases(diseaseModels);
                                            return diseaseModels;
                                        }))
                        .ignoreElement();
        disposable.add(completable
                .subscribe(() -> liveData.setValue(true), this::handleError));
        return liveData;
    }

    public LiveData<List<SpecializationModel>> getAllSpecialization(){
        SpecializationDao specializationDao = AppDatabase.getInstance().getSpecializationDao();
        return specializationDao.getAllSpecialization();
    }

    public LiveData<SpecializationModel> getSpecializationById(String specializationId){
        SpecializationDao specializationDao = AppDatabase.getInstance().getSpecializationDao();
        return specializationDao.getSpecializationById(specializationId);
    }
}
