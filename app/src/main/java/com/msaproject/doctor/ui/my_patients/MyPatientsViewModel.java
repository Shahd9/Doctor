package com.msaproject.doctor.ui.my_patients;

import androidx.lifecycle.LiveData;

import com.msaproject.doctor.base.BaseViewModel;
import com.msaproject.doctor.model.ErrorModel;
import com.msaproject.doctor.model.PatientDoctorModel;
import com.msaproject.doctor.repo.PatientDoctorRepo;
import com.msaproject.doctor.utils.Optional;

import java.util.List;

import javax.inject.Inject;

public class MyPatientsViewModel extends BaseViewModel {

    @Inject
    PatientDoctorRepo patientDoctorRepo;

    @Inject
    public MyPatientsViewModel() {
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {
        addErrorObservers(patientDoctorRepo);
        return super.getErrorLiveData();
    }

    LiveData<List<PatientDoctorModel>> getDoctorPatients() {
        return patientDoctorRepo.getDoctorPatients();
    }

    LiveData<Optional<PatientDoctorModel>> getPatientDoctorIfFound(String patientId) {
        return patientDoctorRepo.getPatientDoctorIfFound(patientId);
    }

    LiveData<PatientDoctorModel> postNewPatientDoctor(PatientDoctorModel patientDoctorModel) {
        return patientDoctorRepo.postNewPatientDoctor(patientDoctorModel);
    }

    @Override
    protected void onCleared() {
        patientDoctorRepo.dispose();
        super.onCleared();
    }
}
