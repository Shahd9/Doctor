package com.msaproject.doctor.ui.appointments;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.msaproject.doctor.R;
import com.msaproject.doctor.base.BaseViewModel;
import com.msaproject.doctor.model.AppointmentImageModel;
import com.msaproject.doctor.model.AppointmentModel;
import com.msaproject.doctor.model.ErrorModel;
import com.msaproject.doctor.repo.AppointmentsRepo;
import com.msaproject.doctor.utils.StringUtils;

import java.util.List;

import javax.inject.Inject;

public class AppointmentsViewModel extends BaseViewModel {

    @Inject
    AppointmentsRepo appointmentsRepo;

    @Inject
    public AppointmentsViewModel() {
    }

    @Override
    public LiveData<ErrorModel> getErrorLiveData() {
        addErrorObservers(appointmentsRepo);
        return super.getErrorLiveData();
    }

    public LiveData<List<AppointmentModel>> getAppointmentsList(String patientId) {
        return appointmentsRepo.getAppointmentsList(patientId);
    }

    public LiveData<List<AppointmentImageModel>> getAppointmentImages(String appointmentId) {
        return appointmentsRepo.getAppointmentImages(appointmentId);
    }

    public LiveData<Boolean> postNewAppointment(AppointmentModel appointmentModel) {
        return appointmentsRepo.postNewAppointment(appointmentModel);
    }

    public LiveData<Boolean> updateAppointment(AppointmentModel appointmentModel) {
        return appointmentsRepo.updateAppointment(appointmentModel);
    }

    public LiveData<Boolean> postNewAppointmentImage(AppointmentImageModel appointmentImageModel) {
        return appointmentsRepo.postNewAppointmentImage(appointmentImageModel);
    }

    public LiveData<Boolean> deleteAppointmentImage(AppointmentImageModel appointmentImageModel) {
        return appointmentsRepo.deleteAppointmentImage(appointmentImageModel);
    }

    public LiveData<String> uploadAppointmentImageAndGetDownloadLink(String photoId, Uri photoUri) {
        MediatorLiveData<String> liveData = new MediatorLiveData<>();
        liveData.addSource(appointmentsRepo.uploadAppointmentImage(photoId, photoUri), uploadStatusResponse -> {
            view.showLoading(StringUtils.getString(R.string.loading_dialog_uploading_msg, uploadStatusResponse.getProgress()));
            if (uploadStatusResponse.getDone()) {
                view.hideLoading();
                liveData.setValue(uploadStatusResponse.getDownloadLink());
            }
        });
        return liveData;
    }

    @Override
    protected void onCleared() {
        appointmentsRepo.dispose();
        super.onCleared();
    }
}
