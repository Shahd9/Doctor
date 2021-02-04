package com.msaproject.doctor.ui.edit_profile;

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

public class EditProfileViewModel extends BaseViewModel {

    @Inject
    SpecializationsAndDiseasesRepo specializationsAndDiseasesRepo;

    @Inject
    UserRepo userRepo;

    @Inject
    public EditProfileViewModel() {
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {
        addErrorObservers(specializationsAndDiseasesRepo);
        addErrorObservers(userRepo);
        return super.getErrorLiveData();
    }

    LiveData<List<SpecializationModel>> getAllSpecialization() {
        return specializationsAndDiseasesRepo.getAllSpecialization();
    }

    LiveData<SpecializationModel> getSpecializationById(String specializationId) {
        return specializationsAndDiseasesRepo.getSpecializationById(specializationId);
    }

    LiveData<Boolean> updateUserData(UserModel model) {
        return userRepo.updateUserData(model);
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

    @Override
    protected void onCleared() {
        specializationsAndDiseasesRepo.dispose();
        userRepo.dispose();
        super.onCleared();
    }
}
