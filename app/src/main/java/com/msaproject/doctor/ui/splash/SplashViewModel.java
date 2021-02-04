package com.msaproject.doctor.ui.splash;

import androidx.lifecycle.LiveData;

import com.msaproject.doctor.base.BaseViewModel;
import com.msaproject.doctor.model.ConfigsModel;
import com.msaproject.doctor.model.ErrorModel;
import com.msaproject.doctor.repo.ConfigsRepo;
import com.msaproject.doctor.repo.SpecializationsAndDiseasesRepo;

import javax.inject.Inject;

public class SplashViewModel extends BaseViewModel {

    @Inject
    ConfigsRepo configsRepo;
    @Inject
    SpecializationsAndDiseasesRepo specializationsAndDiseasesRepo;

    @Inject
    public SplashViewModel() {
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {
        addErrorObservers(configsRepo);
        addErrorObservers(specializationsAndDiseasesRepo);
        return super.getErrorLiveData();
    }

    LiveData<ConfigsModel> getConfigs(){
        return configsRepo.getConfigs();
    }

    LiveData<Boolean> updateDiseasesAndSpecializations(){
        return specializationsAndDiseasesRepo.updateSpecializationsAndDiseases();
    }

    @Override
    protected void onCleared() {
        configsRepo.dispose();
        specializationsAndDiseasesRepo.dispose();
        super.onCleared();
    }
}
