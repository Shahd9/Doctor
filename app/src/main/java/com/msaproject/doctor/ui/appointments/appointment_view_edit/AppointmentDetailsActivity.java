package com.msaproject.doctor.ui.appointments.appointment_view_edit;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.msaproject.doctor.R;
import com.msaproject.doctor.base.BaseActivity;
import com.msaproject.doctor.databinding.ActivityAppointmentDetailsBinding;
import com.msaproject.doctor.model.AppointmentModel;
import com.msaproject.doctor.ui.appointments.AppointmentsViewModel;
import com.msaproject.doctor.utils.StringUtils;

public class AppointmentDetailsActivity extends BaseActivity<ActivityAppointmentDetailsBinding> {

    private static final String APPOINTMENT_MODEL = "AppointmentModel";

    public static Intent getAppointmentDetailsActivityIntent(Context context, AppointmentModel appointmentModel) {
        Intent intent = new Intent(context, AppointmentDetailsActivity.class);
        intent.putExtra(APPOINTMENT_MODEL, appointmentModel);
        return intent;
    }

    private static final int RC_EDIT_APPOINTMENT = 1;

    private AppointmentsViewModel viewModel;
    private AppointmentModel appointmentModel;
    private AppointmentImagesAdapter adapter;

    @Override
    protected ActivityAppointmentDetailsBinding getViewBinding() {
        return ActivityAppointmentDetailsBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppointmentsViewModel.class);
    }

    @Override
    protected void onViewCreated() {
        viewModel.getErrorLiveData().observe(this, this::onError);

        appointmentModel = (AppointmentModel) getIntent().getSerializableExtra(APPOINTMENT_MODEL);
        if (appointmentModel == null) {
            showErrorMsg(StringUtils.getString(R.string.something_went_wrong));
            return;
        }

        adapter = new AppointmentImagesAdapter(AppointmentImagesAdapter.MODE_VIEW, this, null);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        viewBinding.rvAppointmentImages.setLayoutManager(layoutManager);
        viewBinding.rvAppointmentImages.setAdapter(adapter);

        viewBinding.fabEdit.setOnClickListener(v -> startEditAppointmentActivity());

        fillUpAppointmentData();
    }

    private void fillUpAppointmentData() {
        setTitleWithBack(appointmentModel.getTitle());
        viewBinding.tvDoctorNotes.setText(appointmentModel.getDoctorNotes());
        getAppointmentImages();
    }

    private void getAppointmentImages() {
        showLoading();
        viewModel.getAppointmentImages(appointmentModel.getId()).observe(this, appointmentImageModels -> {
            hideLoading();
            adapter.addAll(appointmentImageModels);
        });
    }

    private void startEditAppointmentActivity(){
        startActivityForResult(EditAppointmentActivity.getEditAppointmentActivityIntent(this, appointmentModel), RC_EDIT_APPOINTMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_EDIT_APPOINTMENT && resultCode == RESULT_OK){
            appointmentModel = (AppointmentModel) data.getSerializableExtra(APPOINTMENT_MODEL);
            fillUpAppointmentData();
        }
    }
}