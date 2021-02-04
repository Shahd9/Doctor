package com.msaproject.doctor.ui.complete_register;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.msaproject.doctor.R;
import com.msaproject.doctor.base.BaseViewModel;
import com.msaproject.doctor.model.ErrorModel;
import com.msaproject.doctor.model.SpecializationModel;
import com.msaproject.doctor.model.UserModel;
import com.msaproject.doctor.repo.SpecializationsAndDiseasesRepo;
import com.msaproject.doctor.repo.UserRepo;
import com.msaproject.doctor.utils.StringUtils;

import java.util.List;

import javax.inject.Inject;

public class CompleteRegisterViewModel extends BaseViewModel {

    @Inject
    UserRepo userRepo;

    @Inject
    SpecializationsAndDiseasesRepo specializationsAndDiseasesRepo;

    @Inject
    public CompleteRegisterViewModel() {
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {
        addErrorObservers(userRepo);
        addErrorObservers(specializationsAndDiseasesRepo);
        return super.getErrorLiveData();
    }

    LiveData<Boolean> postNewUser(UserModel model) {
        return userRepo.postNewUser(model);
    }

    LiveData<String> uploadUserPhotoAndGetDownloadLink(Uri photoUri) {
        MediatorLiveData<String> liveData = new MediatorLiveData<>();
        liveData.addSource(userRepo.uploadUserPhoto(photoUri), uploadStatusResponse -> {
            view.showLoading(StringUtils.getString(R.string.loading_dialog_uploading_msg, uploadStatusResponse.getProgress()));
            if (uploadStatusResponse.getDone()) {
                view.hideLoading();
                liveData.setValue(uploadStatusResponse.getDownloadLink());
            }
        });
        return liveData;
    }

    LiveData<List<SpecializationModel>> getAllSpecialization() {
        return specializationsAndDiseasesRepo.getAllSpecialization();
    }

    @Override
    protected void onCleared() {
        userRepo.dispose();
        specializationsAndDiseasesRepo.dispose();
        super.onCleared();
    }
}
