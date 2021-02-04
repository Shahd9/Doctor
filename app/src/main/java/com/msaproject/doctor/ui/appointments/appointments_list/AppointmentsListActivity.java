package com.msaproject.doctor.ui.appointments.appointments_list;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.msaproject.doctor.R;
import com.msaproject.doctor.base.BaseActivity;
import com.msaproject.doctor.databinding.ActivityAppointmentsListBinding;
import com.msaproject.doctor.model.AppointmentModel;
import com.msaproject.doctor.model.PatientDoctorModel;
import com.msaproject.doctor.ui.appointments.AppointmentsViewModel;
import com.msaproject.doctor.ui.appointments.add_appointment.AddAppointmentDialog;
import com.msaproject.doctor.ui.appointments.appointment_view_edit.AppointmentDetailsActivity;
import com.msaproject.doctor.ui.appointments.appointment_view_edit.EditAppointmentActivity;
import com.msaproject.doctor.utils.RandomString;
import com.msaproject.doctor.utils.StringUtils;

public class AppointmentsListActivity extends BaseActivity<ActivityAppointmentsListBinding> {

    private static final String PATIENT_DOCTOR_MODEL = "PatientDoctorModel";

    public static Intent getAppointmentsListActivityIntent(Context context, @NonNull PatientDoctorModel model) {
        Intent intent = new Intent(context, AppointmentsListActivity.class);
        intent.putExtra(PATIENT_DOCTOR_MODEL, model);
        return intent;
    }

    private AppointmentsViewModel viewModel;
    private PatientDoctorModel patientDoctorModel;
    private AppointmentsAdapter adapter;

    @Override
    protected ActivityAppointmentsListBinding getViewBinding() {
        return ActivityAppointmentsListBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppointmentsViewModel.class);
    }

    @Override
    protected void onViewCreated() {
        setTitleWithBack(getString(R.string.patient_appointments));

        patientDoctorModel = (PatientDoctorModel) getIntent().getSerializableExtra(PATIENT_DOCTOR_MODEL);

        viewBinding.fabAdd.setOnClickListener(v -> new AddAppointmentDialog(this, this::addNewAppointment).show());

        adapter = new AppointmentsAdapter(patientDoctorModel.getPatientModel(), this::openAppointmentDetailsActivity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        viewBinding.rvAppointments.setLayoutManager(layoutManager);
        viewBinding.rvAppointments.setAdapter(adapter);

        viewModel.getErrorLiveData().observe(this, this::onError);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAppointments();
    }

    private void getAppointments() {
        showLoading();
        viewModel.getAppointmentsList(patientDoctorModel.getPatientId()).observe(this, appointmentModels -> {
            hideLoading();
            adapter.addAll(appointmentModels);
        });
    }

    private void addNewAppointment(String appointmentTitle) {
        showLoading();
        AppointmentModel model = new AppointmentModel();
        model.setId(new RandomString().nextString());
        model.setPatientId(patientDoctorModel.getPatientId());
        model.setDoctorId(patientDoctorModel.getDoctorId());
        model.setTitle(appointmentTitle);
        viewModel.postNewAppointment(model).observe(this, aBoolean -> {
            hideLoading();
            showSuccessMsg(StringUtils.getString(R.string.appointment_created_successfully));
            openEditAppointmentActivity(model);
        });
    }

    private void openAppointmentDetailsActivity(AppointmentModel model) {
        startActivity(AppointmentDetailsActivity.getAppointmentDetailsActivityIntent(this, model));
    }

    private void openEditAppointmentActivity(AppointmentModel model) {
        startActivity(EditAppointmentActivity.getEditAppointmentActivityIntent(this, model));
    }
}