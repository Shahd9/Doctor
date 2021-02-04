package com.msaproject.doctor.ui.profile;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import com.msaproject.doctor.R;
import com.msaproject.doctor.base.BaseFragment;
import com.msaproject.doctor.databinding.FragmentProfileBinding;
import com.msaproject.doctor.model.UserModel;
import com.msaproject.doctor.model.types.Gender;
import com.msaproject.doctor.pref.UserPref;
import com.msaproject.doctor.ui.edit_profile.EditProfileActivity;
import com.msaproject.doctor.ui.splash.SplashActivity;
import com.msaproject.doctor.ui.view.InfoDialog;
import com.msaproject.doctor.utils.PicassoHelper;
import com.msaproject.doctor.utils.StringUtils;

import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

public class ProfileFragment extends BaseFragment<FragmentProfileBinding> {

    @Inject
    UserPref userPref;

    private ProfileViewModel viewModel;
    private InfoDialog infoDialog;

    @Override
    protected FragmentProfileBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentProfileBinding.inflate(inflater, container, false);
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ProfileViewModel.class);
    }

    @Override
    protected void onViewCreated() {
        setUpViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        fillUpUserData();
    }

    private void setUpViews() {
        viewBinding.ibEditProfile.setOnClickListener(v -> startActivity(new Intent(requireActivity(), EditProfileActivity.class)));
        viewBinding.btnLogout.setOnClickListener(v -> logout());
    }

    private void fillUpUserData() {
        UserModel model = userPref.getUser();
        PicassoHelper.loadImageWithCache(model.getProfilePicLink(), viewBinding.ivImage, PicassoHelper.MODE.FIT_AND_CENTER_CROP,
                null, null, () -> viewBinding.ivImage.setImageResource(R.drawable.ic_user_placeholder));
        viewBinding.tvName.setText(model.getName());
        viewBinding.tvMobile.setText(model.getPhone());
        viewBinding.tvBirthDate.setText(StringUtils.formatDateToLocale(model.getBirthDate(), "dd-MM-yyyy", new Locale(StringUtils.getLanguage())));
        viewModel.getSpecializationById(model.getSpecializationId()).observe(getViewLifecycleOwner(), specializationModel -> viewBinding.tvSpecialization.setText(specializationModel.getName()));
        viewBinding.tvGender.setText(StringUtils.getString(model.getGender() == Gender.MALE ? R.string.radio_male : R.string.radio_female));
    }

    private void logout() {
        infoDialog = new InfoDialog(requireContext(), new InfoDialog.OnDialogActionListener() {
            @Override
            public void onCancel() {
                infoDialog.dismiss();
            }

            @Override
            public void onConfirm() {
                infoDialog.dismiss();

                userPref.logout();

                NotificationManager notificationManager = (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                Objects.requireNonNull(notificationManager).cancelAll();

                Intent intent = new Intent(getContext(), SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        infoDialog.show(R.drawable.warning, StringUtils.getString(R.string.dialog_logout_msg),
                StringUtils.getString(R.string.dialog_logout), StringUtils.getString(R.string.cancel));
    }
}
