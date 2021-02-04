package com.msaproject.doctor.ui.medical_files;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.msaproject.doctor.R;
import com.msaproject.doctor.base.BaseActivity;
import com.msaproject.doctor.databinding.ActivityMedicalFilesBinding;
import com.msaproject.doctor.utils.StringUtils;

public class MedicalFilesActivity extends BaseActivity<ActivityMedicalFilesBinding> {

    private final static String PATIENT_ID = "PatientId";

    public static Intent getMedicalFilesActivityIntent(Context context, String patientId) {
        Intent intent = new Intent(context, MedicalFilesActivity.class);
        intent.putExtra(PATIENT_ID, patientId);
        return intent;
    }

    private MedicalFilesViewModel viewModel;
    private MedicalFilesRecyclerAdapter adapter;

    private String patientId;

    @Override
    protected ActivityMedicalFilesBinding getViewBinding() {
        return ActivityMedicalFilesBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(MedicalFilesViewModel.class);
    }

    @Override
    protected void onViewCreated() {
        setTitleWithBack(StringUtils.getString(R.string.patient_medical_files));

        patientId = getIntent().getStringExtra(PATIENT_ID);

        if (TextUtils.isEmpty(patientId))
            showErrorMsg(StringUtils.getString(R.string.something_went_wrong));
        else {
            setUpViews();
            getPatientMedicalFiles();
        }
    }

    private void setUpViews() {
        adapter = new MedicalFilesRecyclerAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        viewBinding.rvMedicalFiles.setLayoutManager(layoutManager);
        viewBinding.rvMedicalFiles.setAdapter(adapter);
    }

    private void getPatientMedicalFiles() {
        showLoading();
        viewModel.getPatientMedicalFiles(patientId).observe(this, medicalFileModels -> {
            hideLoading();
            adapter.clear(false);
            adapter.addAll(medicalFileModels);
        });
    }

}