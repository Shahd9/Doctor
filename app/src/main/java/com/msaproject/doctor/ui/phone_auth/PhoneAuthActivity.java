package com.msaproject.doctor.ui.phone_auth;

import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.msaproject.doctor.R;
import com.msaproject.doctor.base.BaseActivity;
import com.msaproject.doctor.databinding.ActivityPhoneAuthBinding;
import com.msaproject.doctor.pref.UserPref;
import com.msaproject.doctor.ui.complete_register.CompleteRegisterActivity;
import com.msaproject.doctor.ui.host.HostActivity;
import com.msaproject.doctor.utils.StringUtils;

import javax.inject.Inject;

public class PhoneAuthActivity extends BaseActivity<ActivityPhoneAuthBinding> {

    @Inject
    UserPref userPref;

    private PhoneAuthViewModel viewModel;

    @Override
    protected ActivityPhoneAuthBinding getViewBinding() {
        return ActivityPhoneAuthBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(PhoneAuthViewModel.class);
    }

    @Override
    protected void onViewCreated() {
        setTitle(StringUtils.getString(R.string.phone_auth));
        setUpViews();
        setUpLiveData();
    }

    private void setUpViews() {
        // Phone
        viewBinding.layoutPhone.tvCountryCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        viewBinding.layoutPhone.tvCountryCode.setText("+20");
        viewBinding.layoutPhone.etMobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        StringUtils.setPhoneTextWatcher(viewBinding.layoutPhone.etMobile);
        viewBinding.btnConfirm.setOnClickListener(v -> viewModel.verifyPhoneNumber(this, "+20" + viewBinding.layoutPhone.etMobile.getText().toString()));
        viewBinding.btnCancel.setOnClickListener(v -> finish());
        // Send
        viewBinding.pvPinCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().isEmpty()
                        && editable.toString().trim().length() == 6) {
                    viewModel.validateCode(PhoneAuthActivity.this, editable.toString());
                }
            }
        });
    }

    private void setUpLiveData() {
        viewModel.getOnCodeSent().observe(this, aBoolean -> {
            viewBinding.phoneNumberLayout.setVisibility(View.GONE);
            viewBinding.confirmationNumberLayout.setVisibility(View.VISIBLE);
        });

        viewModel.getOnPhoneAuthenticated().observe(this, userModel -> {
            if (!TextUtils.isEmpty(userModel.getName())) {
                userPref.setUserModel(userModel);
                startActivity(new Intent(PhoneAuthActivity.this, HostActivity.class));
            } else {
                userPref.setUserModel(userModel, false);
                startActivity(new Intent(PhoneAuthActivity.this, CompleteRegisterActivity.class));
            }
            finish();
        });
    }

}