package com.msaproject.doctor.ui.my_patients;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.msaproject.doctor.R;
import com.msaproject.doctor.base.BaseFragment;
import com.msaproject.doctor.databinding.FragmentMyPatientsBinding;
import com.msaproject.doctor.model.PatientDoctorModel;
import com.msaproject.doctor.pref.UserPref;
import com.msaproject.doctor.ui.patient_details.PatientDetailsActivity;
import com.msaproject.doctor.utils.RandomString;
import com.msaproject.doctor.utils.StringUtils;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;

public class MyPatientsFragment extends BaseFragment<FragmentMyPatientsBinding> {

    @Inject
    UserPref userPref;

    private MyPatientsViewModel viewModel;
    private PatientDoctorAdapter adapter;
    private boolean searchBarEnabled;

    @Override
    protected FragmentMyPatientsBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentMyPatientsBinding.inflate(inflater, container, false);
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(MyPatientsViewModel.class);
    }

    @Override
    protected void onViewCreated() {
        setupViews();
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorModel -> showErrorMsg(errorModel.getMessage()));
    }

    @Override
    public void onStart() {
        super.onStart();
        getDoctorPatients();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null)
                showErrorMsg(StringUtils.getString(R.string.scan_cancelled));
            else
                getPatientDoctorIfFound(result.getContents());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setupViews() {
        enableDisableSearchBar(false);
        viewBinding.fabAdd.setOnClickListener(v -> startAddPatientActivity());

        adapter = new PatientDoctorAdapter(this::startPatientDetailsActivity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        viewBinding.rvPatientDoctor.setLayoutManager(layoutManager);
        viewBinding.rvPatientDoctor.setAdapter(adapter);
    }

    private void startAddPatientActivity() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setPrompt(StringUtils.getString(R.string.scan_qr_screen_promt));
        integrator.initiateScan();
    }

    private void startPatientDetailsActivity(PatientDoctorModel model) {
        startActivity(PatientDetailsActivity.getPatientDetailsActivityIntent(requireContext(), model));
    }

    private void enableDisableSearchBar(boolean enabled) {
        searchBarEnabled = enabled;
        viewBinding.etSearch.setEnabled(enabled);
        viewBinding.ivSearch.setEnabled(enabled);
        if (enabled)
            viewBinding.etSearch.setFocusableInTouchMode(true);
        else
            viewBinding.etSearch.setFocusable(false);
    }

    private void getDoctorPatients() {
        showLoading();
        viewModel.getDoctorPatients().observe(getViewLifecycleOwner(), patientDoctorModels -> {
            hideLoading();
            adapter.addAll(patientDoctorModels);
        });
    }

    private void getPatientDoctorIfFound(String patientId) {
        viewModel.getPatientDoctorIfFound(patientId).observe(this, optionalPatientDoctorModel -> {
            if (optionalPatientDoctorModel.isPresent()) {
                hideLoading();
                Toasty.info(requireContext(), StringUtils.getString(R.string.patient_already_exists)).show();
                startPatientDetailsActivity(optionalPatientDoctorModel.get());
            } else
                postNewPatientDoctor(patientId);
        });
    }

    private void postNewPatientDoctor(String patientId) {
        PatientDoctorModel model = new PatientDoctorModel();
        model.setId(new RandomString().nextString());
        model.setDoctorId(userPref.getId());
        model.setPatientId(patientId);

        showLoading();
        viewModel.postNewPatientDoctor(model).observe(getViewLifecycleOwner(), patientDoctorModel -> {
            hideLoading();
            showSuccessMsg(getString(R.string.patient_added_successfully));
            startPatientDetailsActivity(patientDoctorModel);
        });
    }
}
