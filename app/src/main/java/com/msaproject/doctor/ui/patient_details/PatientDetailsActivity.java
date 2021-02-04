package com.msaproject.doctor.ui.patient_details;

import android.content.Context;
import android.content.Intent;

import com.msaproject.doctor.R;
import com.msaproject.doctor.base.BaseActivity;
import com.msaproject.doctor.databinding.ActivityPatientDetailsBinding;
import com.msaproject.doctor.model.PatientDoctorModel;
import com.msaproject.doctor.model.UserModel;
import com.msaproject.doctor.model.types.Gender;
import com.msaproject.doctor.ui.appointments.appointments_list.AppointmentsListActivity;
import com.msaproject.doctor.ui.medical_files.MedicalFilesActivity;
import com.msaproject.doctor.utils.PicassoHelper;
import com.msaproject.doctor.utils.StringUtils;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.util.Locale;

public class PatientDetailsActivity extends BaseActivity<ActivityPatientDetailsBinding> {

    private final static String PATIENT_DOCTOR_MODEL = "PatientDoctorModel";

    public static Intent getPatientDetailsActivityIntent(Context context, PatientDoctorModel patientDoctorModel) {
        Intent intent = new Intent(context, PatientDetailsActivity.class);
        intent.putExtra(PATIENT_DOCTOR_MODEL, patientDoctorModel);
        return intent;
    }

    private PatientDoctorModel patientDoctorModel;
    private UserModel patientModel;

    @Override
    protected ActivityPatientDetailsBinding getViewBinding() {
        return ActivityPatientDetailsBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
    }

    @Override
    protected void onViewCreated() {
        setTitleWithBack(StringUtils.getString(R.string.patient_details));
        if (getDataFromIntent()) {
            setUpViews();
            fillUpUserData();
        }
    }

    private boolean getDataFromIntent() {
        try {
            patientDoctorModel = ((PatientDoctorModel) getIntent().getSerializableExtra(PATIENT_DOCTOR_MODEL));
            patientModel = patientDoctorModel.getPatientModel();
            return patientModel != null;
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMsg(StringUtils.getString(R.string.something_went_wrong));
            return false;
        }
    }

    private void setUpViews() {
        viewBinding.clImageHolder.setOnClickListener(v ->
                new StfalconImageViewer.Builder<>(this, new String[]{patientModel.getProfilePicLink()}, (iv, image) ->
                        Picasso.get().load(image).into(iv))
                        .withTransitionFrom(viewBinding.ivImage)
                        .withBackgroundColorResource(R.color.colorBlack).show(true));

        viewBinding.cvPatientMedicalFiles.setOnClickListener(v -> startActivity(MedicalFilesActivity.getMedicalFilesActivityIntent(this, patientModel.getId())));
        viewBinding.cvPatientAppointments.setOnClickListener(v -> startActivity(AppointmentsListActivity.getAppointmentsListActivityIntent(this, patientDoctorModel)));
    }

    private void fillUpUserData() {
        PicassoHelper.loadImageWithCache(patientModel.getProfilePicLink(), viewBinding.ivImage, PicassoHelper.MODE.JUST_INTO, null, null, null);
        viewBinding.tvName.setText(patientModel.getName());
        viewBinding.tvMobile.setText(patientModel.getPhone());
        viewBinding.tvWeight.setText(StringUtils.getString(R.string.weight_placeholder, patientModel.getWeight()));
        viewBinding.tvHeight.setText(StringUtils.getString(R.string.height_placeholder, patientModel.getHeight()));
        viewBinding.tvBirthDate.setText(StringUtils.formatDateToLocale(patientModel.getBirthDate(), "dd-MM-yyyy", new Locale(StringUtils.getLanguage())));
        viewBinding.tvGender.setText(StringUtils.getString(patientModel.getGender() == Gender.MALE ? R.string.radio_male : R.string.radio_female));
    }

}