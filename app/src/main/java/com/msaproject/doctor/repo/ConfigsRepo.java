package com.msaproject.doctor.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.msaproject.doctor.base.BaseRepo;
import com.msaproject.doctor.model.ConfigsModel;
import com.msaproject.doctor.network.Endpoints;
import com.msaproject.doctor.utils.Optional;

import javax.inject.Inject;

import io.reactivex.Single;

public class ConfigsRepo extends BaseRepo {

    @Inject
    public ConfigsRepo() {
    }

    public LiveData<ConfigsModel> getConfigs() {
        MutableLiveData<ConfigsModel> liveData = new MutableLiveData<>();
        Single<ConfigsModel> single = firebaseManager
                .getDocumentSnapshot(Endpoints.COLLECTION_STATICS, Endpoints.DOCUMENT_APP_CURRENT_CONFIGS, ConfigsModel.class)
                .map(Optional::get);
        disposable.add(single
                .subscribe(liveData::setValue, this::handleError));
        return liveData;
    }
}
