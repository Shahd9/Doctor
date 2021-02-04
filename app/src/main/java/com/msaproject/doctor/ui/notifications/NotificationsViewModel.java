package com.msaproject.doctor.ui.notifications;

import androidx.lifecycle.LiveData;

import com.msaproject.doctor.base.BaseViewModel;
import com.msaproject.doctor.model.ErrorModel;

import javax.inject.Inject;

public class NotificationsViewModel extends BaseViewModel {

    @Inject
    public NotificationsViewModel() {
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {
        return super.getErrorLiveData();
    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }
}
