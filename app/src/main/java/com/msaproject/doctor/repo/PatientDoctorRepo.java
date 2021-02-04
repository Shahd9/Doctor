package com.msaproject.doctor.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.msaproject.doctor.Constants;
import com.msaproject.doctor.base.BaseRepo;
import com.msaproject.doctor.model.PatientDoctorModel;
import com.msaproject.doctor.model.UserModel;
import com.msaproject.doctor.network.Endpoints;
import com.msaproject.doctor.network.QueryBuilder;
import com.msaproject.doctor.utils.Optional;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class PatientDoctorRepo extends BaseRepo {

    @Inject
    public PatientDoctorRepo() {
    }

    public LiveData<List<PatientDoctorModel>> getDoctorPatients() {
        MutableLiveData<List<PatientDoctorModel>> liveData = new MutableLiveData<>();
        QueryBuilder queryBuilder = new QueryBuilder(firebaseManager, Endpoints.COLLECTION_PATIENT_DOCTOR)
                .addOperationToQuery(PatientDoctorModel.DOCTOR_ID, QueryBuilder.Operators.OPERATOR_EQUAL, userPref.getId())
                .addOrderingToQuery(Constants.MAP_KEY_UPDATED_AT, QueryBuilder.OrderingDirections.DIRECTION_DESC);

        Single<List<PatientDoctorModel>> single =
                firebaseManager.getDocumentsByQuery(queryBuilder, PatientDoctorModel.class)
                        .flattenAsObservable(patientDoctorModels -> patientDoctorModels)
                        .flatMap(patientDoctorModel ->
                                firebaseManager.getDocumentSnapshot(Endpoints.COLLECTION_USERS, patientDoctorModel.getPatientId(), UserModel.class)
                                        .toObservable()
                                        .map(userModelOptional -> {
                                            patientDoctorModel.setPatientModel(userModelOptional.orElse(null));
                                            return patientDoctorModel;
                                        }))
                        .toList();

        disposable.add(single
                .subscribe(liveData::setValue, this::handleError));
        return liveData;
    }

    public LiveData<Optional<PatientDoctorModel>> getPatientDoctorIfFound(String patientId) {
        MutableLiveData<Optional<PatientDoctorModel>> liveData = new MutableLiveData<>();
        QueryBuilder queryBuilder = new QueryBuilder(firebaseManager, Endpoints.COLLECTION_PATIENT_DOCTOR)
                .addOperationToQuery(PatientDoctorModel.DOCTOR_ID, QueryBuilder.Operators.OPERATOR_EQUAL, userPref.getId())
                .addOperationToQuery(PatientDoctorModel.PATIENT_ID, QueryBuilder.Operators.OPERATOR_EQUAL, patientId);

        Single<Optional<PatientDoctorModel>> single = firebaseManager.getDocumentsByQuery(queryBuilder, PatientDoctorModel.class)
                .map(patientDoctorModels -> (Optional.ofNullable(patientDoctorModels.isEmpty() ? null : patientDoctorModels.get(0))))
                .flatMap(optionalPatientDoctor -> optionalPatientDoctor.isPresent()
                        ? firebaseManager.getDocumentSnapshot(Endpoints.COLLECTION_USERS, optionalPatientDoctor.get().getPatientId(), UserModel.class)
                        .map(optionalUserModel -> {
                            optionalPatientDoctor.get().setPatientModel(optionalUserModel.orElse(null));
                            return optionalPatientDoctor;
                        })
                        : Single.just(optionalPatientDoctor));

        disposable.add(single
                .subscribe(liveData::setValue, this::handleError));
        return liveData;
    }

    public LiveData<PatientDoctorModel> postNewPatientDoctor(PatientDoctorModel patientDoctorModel) {
        Map<String, Object> patientDoctorHashMap = patientDoctorModel.getModelMap();
        firebaseManager.useCreatedAtServerTime(patientDoctorHashMap);
        firebaseManager.useUpdatedAtServerTime(patientDoctorHashMap);
        MutableLiveData<PatientDoctorModel> liveData = new MutableLiveData<>();
        Single<PatientDoctorModel> single = firebaseManager
                .setValueToDocument(Endpoints.COLLECTION_PATIENT_DOCTOR, patientDoctorModel.getId(), patientDoctorHashMap)
                .toSingleDefault(true)
                .flatMap(aBoolean -> firebaseManager.getDocumentSnapshot(Endpoints.COLLECTION_USERS, patientDoctorModel.getPatientId(), UserModel.class))
                .map(optionalUserModel -> {
                    patientDoctorModel.setPatientModel(optionalUserModel.orElse(null));
                    return patientDoctorModel;
                });
        disposable.add(single
                .subscribe(liveData::setValue, this::handleError));
        return liveData;
    }

    public LiveData<Boolean> updatePatientDoctor(PatientDoctorModel patientDoctorModel) {
        Map<String, Object> patientDoctorHashMap = patientDoctorModel.getModelMap();
        firebaseManager.useUpdatedAtServerTime(patientDoctorHashMap);
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        Completable completable = firebaseManager
                .updateDocument(Endpoints.COLLECTION_PATIENT_DOCTOR, patientDoctorModel.getId(), patientDoctorHashMap);
        disposable.add(completable
                .subscribe(() -> liveData.setValue(true), this::handleError));
        return liveData;
    }
}
