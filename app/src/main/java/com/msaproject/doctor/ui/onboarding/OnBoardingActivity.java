package com.msaproject.doctor.ui.onboarding;

import android.content.Intent;

import com.msaproject.doctor.base.BaseActivity;
import com.msaproject.doctor.databinding.ActivityOnboardingBinding;
import com.msaproject.doctor.pref.SettingPref;
import com.msaproject.doctor.ui.phone_auth.PhoneAuthActivity;

import javax.inject.Inject;

public class OnBoardingActivity extends BaseActivity<ActivityOnboardingBinding> {

    @Inject
    SettingPref settingPref;

    @Override
    protected ActivityOnboardingBinding getViewBinding() {
        return ActivityOnboardingBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
    }

    @Override
    protected void onViewCreated() {
        viewBinding.viewPager.setAdapter(new OnBoardingPagerAdapter(this, this::gotoPhoneAuth));
        viewBinding.dotsIndicator.setViewPager2(viewBinding.viewPager);
        viewBinding.tvLogin.setOnClickListener(v -> gotoPhoneAuth());
    }

    public void gotoPhoneAuth(){
        settingPref.setShowTutorial(false);
        startActivity(new Intent(this, PhoneAuthActivity.class));
        finish();
    }

}