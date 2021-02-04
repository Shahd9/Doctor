package com.msaproject.doctor.ui.appointments.appointment_view_edit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.msaproject.doctor.R;
import com.msaproject.doctor.base.BaseActivity;
import com.msaproject.doctor.databinding.ActivityEditAppointmentBinding;
import com.msaproject.doctor.model.AppointmentImageModel;
import com.msaproject.doctor.model.AppointmentModel;
import com.msaproject.doctor.ui.appointments.AppointmentsViewModel;
import com.msaproject.doctor.utils.RandomString;
import com.msaproject.doctor.utils.StringUtils;

import java.io.File;
import java.util.Objects;

public class EditAppointmentActivity extends BaseActivity<ActivityEditAppointmentBinding> {

    private static final String APPOINTMENT_MODEL = "AppointmentModel";

    public static Intent getEditAppointmentActivityIntent(Context context, AppointmentModel appointmentModel) {
        Intent intent = new Intent(context, EditAppointmentActivity.class);
        intent.putExtra(APPOINTMENT_MODEL, appointmentModel);
        return intent;
    }

    private AppointmentsViewModel viewModel;
    private AppointmentModel appointmentModel;
    private AppointmentImagesAdapter adapter;

    @Override
    protected ActivityEditAppointmentBinding getViewBinding() {
        return ActivityEditAppointmentBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AppointmentsViewModel.class);
    }

    @Override
    protected void onViewCreated() {
        setTitleWithBack(StringUtils.getString(R.string.edit_appointment));
        appointmentModel = (AppointmentModel) getIntent().getSerializableExtra(APPOINTMENT_MODEL);

        adapter = new AppointmentImagesAdapter(AppointmentImagesAdapter.MODE_EDIT, this, this::deleteAppointmentImage);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        viewBinding.rvAppointmentImages.setLayoutManager(layoutManager);
        viewBinding.rvAppointmentImages.setAdapter(adapter);

        viewModel.getErrorLiveData().observe(this, this::onError);

        if (fillAppointmentData())
            getAppointmentImages();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                showErrorMsg(getString(R.string.no_image_selected));
                return;
            }

            try {
                File photoFile = Objects.requireNonNull(ImagePicker.Companion.getFile(data));
                uploadAppointmentImage(Uri.fromFile(photoFile));
            } catch (NullPointerException e) {
                showErrorMsg(getString(R.string.something_went_wrong));
                e.printStackTrace();
            }

        }
    }

    private boolean fillAppointmentData() {
        if (appointmentModel == null) {
            showErrorMsg(StringUtils.getString(R.string.something_went_wrong));
            return false;
        }
        viewBinding.etTitle.setText(appointmentModel.getTitle());
        viewBinding.etNotes.setText(appointmentModel.getDoctorNotes());

        viewBinding.fabAdd.setOnClickListener(v -> ImagePicker
                .Companion
                .with(this)
                .crop()
                .start());

        viewBinding.btnApplyChanges.setOnClickListener(v -> updateAppointmentChanges());

        return true;
    }

    private void getAppointmentImages() {
        showLoading();
        viewModel.getAppointmentImages(appointmentModel.getId()).observe(this, appointmentImageModels -> {
            hideLoading();
            adapter.addAll(appointmentImageModels);
        });
    }

    private void uploadAppointmentImage(Uri photoUri) {
        String photoId = new RandomString().nextString();
        viewModel.uploadAppointmentImageAndGetDownloadLink(photoId, photoUri).observe(this, downloadLink -> {
            AppointmentImageModel model = new AppointmentImageModel();
            model.setId(photoId);
            model.setAppointmentId(appointmentModel.getId());
            model.setDownloadLink(downloadLink);
            postNewAppointmentImage(model);
        });
    }

    private void updateAppointmentChanges() {
        String title = viewBinding.etTitle.getText().toString().trim();
        String notes = viewBinding.etNotes.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            showErrorMsg(StringUtils.getString(R.string.appointment_title_cannot_be_empty));
            return;
        }
        appointmentModel.setTitle(title);
        appointmentModel.setDoctorNotes(notes);

        showLoading();
        viewModel.updateAppointment(appointmentModel).observe(this, aBoolean -> {
            hideLoading();
            showSuccessMsg(StringUtils.getString(R.string.appointment_data_updated_successfully));
            Intent intent = new Intent();
            intent.putExtra(APPOINTMENT_MODEL, appointmentModel);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private void postNewAppointmentImage(AppointmentImageModel model) {
        showLoading();
        viewModel.postNewAppointmentImage(model).observe(this, aBoolean -> getAppointmentImages());
    }

    private void deleteAppointmentImage(AppointmentImageModel model) {
        showLoading();
        viewModel.deleteAppointmentImage(model).observe(this, aBoolean -> getAppointmentImages());
    }
}