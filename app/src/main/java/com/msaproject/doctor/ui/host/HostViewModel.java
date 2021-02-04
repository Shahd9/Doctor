package com.msaproject.doctor.ui.host;

import androidx.lifecycle.LiveData;

import com.msaproject.doctor.base.BaseViewModel;
import com.msaproject.doctor.model.ErrorModel;

import javax.inject.Inject;

public class HostViewModel extends BaseViewModel {


    @Inject
    public HostViewModel() {
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {;
        return super.getErrorLiveData();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
