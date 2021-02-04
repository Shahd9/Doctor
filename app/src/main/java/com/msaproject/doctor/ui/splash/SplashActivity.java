package com.msaproject.doctor.ui.splash;

import android.content.Intent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.lifecycle.ViewModelProvider;

import com.msaproject.doctor.R;
import com.msaproject.doctor.base.BaseActivity;
import com.msaproject.doctor.databinding.ActivitySplashBinding;
import com.msaproject.doctor.pref.SettingPref;
import com.msaproject.doctor.pref.UserPref;
import com.msaproject.doctor.ui.host.HostActivity;
import com.msaproject.doctor.ui.onboarding.OnBoardingActivity;
import com.msaproject.doctor.ui.phone_auth.PhoneAuthActivity;
import com.msaproject.doctor.utils.StringUtils;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {

    @Inject
    UserPref userPref;
    @Inject
    SettingPref settingPref;

    private SplashViewModel viewModel;

    @Override
    protected ActivitySplashBinding getViewBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(SplashViewModel.class);
        viewModel.getErrorLiveData().observe(this, errorModel -> Toasty.error(SplashActivity.this,
                StringUtils.getString(errorModel.isNetworkError()
                        ? R.string.no_internet_connection
                        : R.string.an_error_has_occurred))
                .show());
    }

    @Override
    protected void onViewCreated() {
        startAnimation();
        getConfigs();
    }

    private void startAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(600);
        viewBinding.ivLogo.setAnimation(animation);
    }

    private void getConfigs() {
        viewModel.getConfigs().observe(this, configsModel -> {
            if (configsModel.getDatabaseVersion() > settingPref.getFirestoreDatabaseVersion())
                getDiseases();
            else
                navigate();
        });
    }

    private void getDiseases() {
        viewModel.updateDiseasesAndSpecializations().observe(this, aBoolean -> navigate());
    }

    private void navigate() {
        Intent intent;
        if (userPref.isLoggedIn()) {
            intent = new Intent(this, HostActivity.class);
        } else if (settingPref.shouldShowTutorial()) {
            intent = new Intent(this, OnBoardingActivity.class);
        } else {
            intent = new Intent(this, PhoneAuthActivity.class);
        }
        startActivity(intent);
        finish();
    }

}