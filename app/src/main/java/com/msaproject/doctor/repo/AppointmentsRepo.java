package com.msaproject.doctor.repo;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.msaproject.doctor.Constants;
import com.msaproject.doctor.base.BaseRepo;
import com.msaproject.doctor.model.AppointmentImageModel;
import com.msaproject.doctor.model.AppointmentModel;
import com.msaproject.doctor.model.response.UploadStatusResponse;
import com.msaproject.doctor.network.Endpoints;
import com.msaproject.doctor.network.QueryBuilder;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class AppointmentsRepo extends BaseRepo {

    @Inject
    public AppointmentsRepo() {
    }

    public LiveData<List<AppointmentModel>> getAppointmentsList(String patientId) {
        MutableLiveData<List<AppointmentModel>> liveData = new MutableLiveData<>();
        QueryBuilder queryBuilder = new QueryBuilder(firebaseManager, Endpoints.COLLECTION_APPOINTMENTS)
                .addOperationToQuery(AppointmentModel.DOCTOR_ID, QueryBuilder.Operators.OPERATOR_EQUAL, userPref.getId())
                .addOperationToQuery(AppointmentModel.PATIENT_ID, QueryBuilder.Operators.OPERATOR_EQUAL, patientId)
                .addOrderingToQuery(Constants.MAP_KEY_CREATED_AT, QueryBuilder.OrderingDirections.DIRECTION_DESC);
        Single<List<AppointmentModel>> single =
                firebaseManager.getDocumentsByQuery(queryBuilder, AppointmentModel.class);
        disposable.add(single
                .subscribe(liveData::setValue, this::handleError));
        return liveData;
    }

    public LiveData<List<AppointmentImageModel>> getAppointmentImages(String appointmentId) {
        MutableLiveData<List<AppointmentImageModel>> liveData = new MutableLiveData<>();
        QueryBuilder queryBuilder = new QueryBuilder(firebaseManager, Endpoints.COLLECTION_APPOINTMENT_IMAGES)
                .addOperationToQuery(AppointmentImageModel.APPOINTMENT_ID, QueryBuilder.Operators.OPERATOR_EQUAL, appointmentId)
                .addOrderingToQuery(Constants.MAP_KEY_CREATED_AT, QueryBuilder.OrderingDirections.DIRECTION_DESC);
        Single<List<AppointmentImageModel>> single =
                firebaseManager.getDocumentsByQuery(queryBuilder, AppointmentImageModel.class);
        disposable.add(single
                .subscribe(liveData::setValue, this::handleError));
        return liveData;
    }

    public LiveData<Boolean> postNewAppointment(AppointmentModel appointmentModel) {
        Map<String, Object> appointmentHashMap = appointmentModel.getModelMap();
        firebaseManager.useCreatedAtServerTime(appointmentHashMap);
        firebaseManager.useUpdatedAtServerTime(appointmentHashMap);
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        Completable single = firebaseManager
                .setValueToDocument(Endpoints.COLLECTION_APPOINTMENTS, appointmentModel.getId(), appointmentHashMap);
        disposable.add(single
                .subscribe(() -> liveData.setValue(true), this::handleError));
        return liveData;
    }

    public LiveData<Boolean> updateAppointment(AppointmentModel appointmentModel) {
        Map<String, Object> appointmentHashMap = appointmentModel.getModelMap();
        appointmentHashMap.remove(Constants.MAP_KEY_CREATED_AT);
        firebaseManager.useUpdatedAtServerTime(appointmentHashMap);
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        Completable single = firebaseManager
                .updateDocument(Endpoints.COLLECTION_APPOINTMENTS, appointmentModel.getId(), appointmentHashMap);
        disposable.add(single
                .subscribe(() -> liveData.setValue(true), this::handleError));
        return liveData;
    }

    public LiveData<Boolean> postNewAppointmentImage(AppointmentImageModel appointmentImageModel) {
        Map<String, Object> appointmentHashMap = appointmentImageModel.getModelMap();
        firebaseManager.useCreatedAtServerTime(appointmentHashMap);
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        Completable single = firebaseManager
                .setValueToDocument(Endpoints.COLLECTION_APPOINTMENT_IMAGES, appointmentImageModel.getId(), appointmentHashMap);
        disposable.add(single
                .subscribe(() -> liveData.setValue(true), this::handleError));
        return liveData;
    }

    public LiveData<UploadStatusResponse> uploadAppointmentImage(String photoId, Uri photoUri) {
        MutableLiveData<UploadStatusResponse> liveData = new MutableLiveData<>();
        Observable<UploadStatusResponse> observable = firebaseManager
                .uploadFileWithProgressObservable(Endpoints.FOLDER_APPOINTMENT_IMAGES, photoId, Constants.STORAGE_EXTENSION_DEFAULT, photoUri);
        disposable.add(observable.subscribe(liveData::setValue, this::handleError));
        return liveData;
    }

    public LiveData<Boolean> deleteAppointmentImage(AppointmentImageModel appointmentImageModel) {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        Completable completable = firebaseManager
                .deleteFileFromStorage(appointmentImageModel.getDownloadLink())
                .concatWith(firebaseManager.deleteDocument(Endpoints.COLLECTION_APPOINTMENT_IMAGES, appointmentImageModel.getId()));
        disposable.add(completable.subscribe(() -> liveData.setValue(true), this::handleError));
        return liveData;
    }
}
