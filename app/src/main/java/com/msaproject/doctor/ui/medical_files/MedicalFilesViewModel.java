package com.msaproject.doctor.ui.medical_files;

import androidx.lifecycle.LiveData;

import com.msaproject.doctor.base.BaseViewModel;
import com.msaproject.doctor.model.ErrorModel;
import com.msaproject.doctor.model.MedicalFileModel;
import com.msaproject.doctor.repo.MedicalFilesRepo;

import java.util.List;

import javax.inject.Inject;

public class MedicalFilesViewModel extends BaseViewModel {

    @Inject
    MedicalFilesRepo medicalFilesRepo;

    @Inject
    public MedicalFilesViewModel() {
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {
        addErrorObservers(medicalFilesRepo);
        return super.getErrorLiveData();
    }

    LiveData<List<MedicalFileModel>> getPatientMedicalFiles(String patientId) {
        return medicalFilesRepo.getPatientMedicalFiles(patientId);
    }

    @Override
    protected void onCleared() {
        medicalFilesRepo.dispose();
        super.onCleared();
    }
}
