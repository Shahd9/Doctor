package com.msaproject.doctor.ui.edit_profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.msaproject.doctor.R;
import com.msaproject.doctor.base.BaseActivity;
import com.msaproject.doctor.databinding.ActivityEditProfileBinding;
import com.msaproject.doctor.model.UserModel;
import com.msaproject.doctor.model.types.Gender;
import com.msaproject.doctor.pref.UserPref;
import com.msaproject.doctor.ui.specializations_selection.SelectSpecializationDialog;
import com.msaproject.doctor.utils.PicassoHelper;
import com.msaproject.doctor.utils.StringUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

public class EditProfileActivity extends BaseActivity<ActivityEditProfileBinding> {

    @Inject
    UserPref userPref;

    private EditProfileViewModel viewModel;
    private UserModel currentUserModel;
    private Integer year, monthOfYear, dayOfMonth;
    private SelectSpecializationDialog specializationsDialog;
    @Gender
    private int gender;
    boolean photoStateChanged = false;
    boolean isThereAPhoto;
    private Uri userPhoto;

    @Override
    protected ActivityEditProfileBinding getViewBinding() {
        return ActivityEditProfileBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(EditProfileViewModel.class);
    }

    @Override
    protected void onViewCreated() {
        setTitleWithBack(StringUtils.getString(R.string.edit_profile));
        currentUserModel = userPref.getUser();
        fillUpUserData();

        viewModel.getErrorLiveData().observe(this, this::onError);

        viewModel.getAllSpecialization().observe(this, specializationModels -> specializationsDialog = new SelectSpecializationDialog(this, specializationModels, currentUserModel.getSpecializationId(),
                specializationModel -> viewBinding.tvSpecialization.setText(specializationModel.getName())));

        viewBinding.ibAddImage.setOnClickListener(v -> ImagePicker
                .Companion
                .with(this)
                .crop()
                .start());

        viewBinding.ibRemoveImage.setOnClickListener(v -> removeImage());
        viewBinding.clDate.setOnClickListener(v -> showDatePicker());
        viewBinding.clSpecialization.setOnClickListener(v -> showSpecializationsDialog());
        viewBinding.rgGender.setOnCheckedChangeListener((group, checkedId) -> gender = (checkedId == viewBinding.rbMale.getId()) ? Gender.MALE : Gender.FEMALE);
        viewBinding.btnApplyChanges.setOnClickListener(v -> {
            if (!validateInput())
                return;
            if (photoStateChanged) {
                if (isThereAPhoto)
                    viewModel.uploadUserPhotoAndGetDownloadLink(userPhoto).observe(this, downloadLink -> {
                        currentUserModel.setProfilePicLink(downloadLink);
                        updateData();
                    });
                else {
                    currentUserModel.setProfilePicLink(null);
                    updateData();
                }
            } else
                updateData();

        });
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
                    photoStateChanged = true;
                    isThereAPhoto = true;
                    userPhoto = Uri.fromFile(photoFile);
                    Picasso.get().load((photoFile)).fit().centerCrop().into(viewBinding.ivImage);
                    viewBinding.ibRemoveImage.setVisibility(View.VISIBLE);
                } catch (NullPointerException e) {
                    showErrorMsg(getString(R.string.something_went_wrong));
                    e.printStackTrace();
                }

            }
        }

    private void fillUpUserData() {
        PicassoHelper.loadImageWithCache(currentUserModel.getProfilePicLink(), viewBinding.ivImage, PicassoHelper.MODE.FIT_AND_CENTER_CROP, null, null, null);
        viewBinding.etName.setText(currentUserModel.getName());
        viewBinding.tvDate.setText(StringUtils.formatDateToLocale(currentUserModel.getBirthDate(), "dd-MM-yyyy", new Locale(StringUtils.getLanguage())));
        viewModel.getSpecializationById(currentUserModel.getSpecializationId()).observe(this, specializationModel -> viewBinding.tvSpecialization.setText(specializationModel.getName()));
        viewBinding.rgGender.check(currentUserModel.getGender() == Gender.MALE ? viewBinding.rbMale.getId() : viewBinding.rbFemale.getId());

        isThereAPhoto = currentUserModel.getProfilePicLink() != null;
        viewBinding.ibRemoveImage.setVisibility(isThereAPhoto ? View.VISIBLE : View.GONE);

        gender = currentUserModel.getGender();

        Calendar c = Calendar.getInstance();
        c.setTime(currentUserModel.getBirthDate());
        year = c.get(Calendar.YEAR);
        monthOfYear = c.get(Calendar.MONTH);
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
    }

    private void removeImage() {
        photoStateChanged = true;
        isThereAPhoto = false;
        userPhoto = null;
        viewBinding.ivImage.setImageResource(R.drawable.ic_user_placeholder);
        viewBinding.ibRemoveImage.setVisibility(View.GONE);
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        int year2, monthOfYear2, dayOfMonth2;
        year2 = year;
        monthOfYear2 = monthOfYear;
        dayOfMonth2 = dayOfMonth;
        DatePickerDialog mDate = new DatePickerDialog(this, (view, y, m, d) -> {

            String selectedDate = y + "-" + (m + 1 < 10 ? "0" : "") + (m + 1) + "-"
                    + (d < 10 ? "0" : "") + d;

            year = y;
            monthOfYear = m;
            dayOfMonth = d;
            viewBinding.tvDate.setText(StringUtils.formatDateToLocale(selectedDate, "dd-MM-yyyy", new Locale(StringUtils.getLanguage())));
        }, year2, monthOfYear2, dayOfMonth2);
        c.set(1800, 0, 1);
        mDate.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        mDate.show();
    }

    private void showSpecializationsDialog() {
        if (specializationsDialog != null)
            specializationsDialog.show(getSupportFragmentManager(), "SpecializationsDialog");
        else
            showErrorMsg(StringUtils.getString(R.string.something_went_wrong));
    }

    private boolean validateInput() {
        hideKeyboard();

        // Name
        String name = viewBinding.etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            showErrorMsg(StringUtils.getString(R.string.please_enter_valid_name));
            return false;
        }
        // BirthDateInMillis
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);

        currentUserModel.setName(name);
        currentUserModel.setBirthDate(c.getTime());
        currentUserModel.setSpecializationId(specializationsDialog.getSelectedId());
        currentUserModel.setGender(gender);

        return true;
    }

    private void updateData() {
        showLoading();
        viewModel.updateUserData(currentUserModel).observe(this, aBoolean -> {
            hideLoading();
            showSuccessMsg("Your Data Got Updated Successfully");
            userPref.setUserModel(currentUserModel);
            finish();
        });
    }
}