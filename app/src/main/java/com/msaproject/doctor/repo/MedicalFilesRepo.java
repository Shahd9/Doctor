package com.msaproject.doctor.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.msaproject.doctor.Constants;
import com.msaproject.doctor.base.BaseRepo;
import com.msaproject.doctor.model.MedicalFileModel;
import com.msaproject.doctor.network.Endpoints;
import com.msaproject.doctor.network.QueryBuilder;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class MedicalFilesRepo extends BaseRepo {

    @Inject
    public MedicalFilesRepo() {
    }

    public LiveData<List<MedicalFileModel>> getPatientMedicalFiles(String patientId) {
        MutableLiveData<List<MedicalFileModel>> liveData = new MutableLiveData<>();
        QueryBuilder queryBuilder = new QueryBuilder(firebaseManager, Endpoints.COLLECTION_MEDICAL_FILES)
                .addOperationToQuery(MedicalFileModel.PATIENT_ID, QueryBuilder.Operators.OPERATOR_EQUAL, patientId)
                .addOrderingToQuery(Constants.MAP_KEY_CREATED_AT, QueryBuilder.OrderingDirections.DIRECTION_DESC);
        Single<List<MedicalFileModel>> single =
                firebaseManager.getDocumentsByQuery(queryBuilder, MedicalFileModel.class);
        disposable.add(single
                .subscribe(liveData::setValue, this::handleError));
        return liveData;
    }
}
