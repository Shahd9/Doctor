package com.msaproject.doctor.ui.profile;

import androidx.lifecycle.LiveData;

import com.msaproject.doctor.base.BaseViewModel;
import com.msaproject.doctor.model.ErrorModel;
import com.msaproject.doctor.model.SpecializationModel;
import com.msaproject.doctor.repo.SpecializationsAndDiseasesRepo;

import javax.inject.Inject;

public class ProfileViewModel extends BaseViewModel {

    @Inject
    SpecializationsAndDiseasesRepo specializationsAndDiseasesRepo;

    @Inject
    public ProfileViewModel() {
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {
        addErrorObservers(specializationsAndDiseasesRepo);
        return super.getErrorLiveData();
    }

    LiveData<SpecializationModel> getSpecializationById(String specializationId) {
        return specializationsAndDiseasesRepo.getSpecializationById(specializationId);
    }

    @Override
    protected void onCleared() {
        specializationsAndDiseasesRepo.dispose();
        super.onCleared();
    }

}
