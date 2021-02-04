package com.msaproject.doctor.ui.phone_auth;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.msaproject.doctor.R;
import com.msaproject.doctor.base.BaseViewModel;
import com.msaproject.doctor.model.ErrorModel;
import com.msaproject.doctor.model.UserModel;
import com.msaproject.doctor.repo.PhoneAuthRepo;
import com.msaproject.doctor.repo.UserRepo;
import com.msaproject.doctor.utils.StringUtils;

import java.util.Objects;

import javax.inject.Inject;

public class PhoneAuthViewModel extends BaseViewModel {

    private final MediatorLiveData<UserModel> onPhoneAuthenticated;
    private final MutableLiveData<Boolean> onCodeSent;
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks1;
    private final OnCompleteListener<AuthResult> callbacks2;
    private String verificationId;

    @SuppressLint("StaticFieldLeak")
    private Activity activityInstance;

    @Inject
    PhoneAuthRepo phoneAuthRepo;

    @Inject
    UserRepo userRepo;

    MutableLiveData<UserModel> getOnPhoneAuthenticated() {
        return onPhoneAuthenticated;
    }

    MutableLiveData<Boolean> getOnCodeSent() {
        return onCodeSent;
    }

    void verifyPhoneNumber(Activity activity, String phoneNumber) {
        this.activityInstance = activity;
        view.showLoading();
        phoneAuthRepo.verifyPhoneNumber(activity, callbacks1, phoneNumber);
    }

    void validateCode(Activity activity, String code) {
        this.activityInstance = activity;
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredentials(credential);
    }

    @Inject
    public PhoneAuthViewModel() {
        onPhoneAuthenticated = new MediatorLiveData<>();
        onCodeSent = new MutableLiveData<>();
        callbacks1 = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithCredentials(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                view.hideLoading();
                view.showErrorMsg(StringUtils.getString(R.string.invalid_phone_number));
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                view.hideLoading();
                onCodeSent.setValue(true);
                PhoneAuthViewModel.this.verificationId = verificationId;
                super.onCodeSent(verificationId, forceResendingToken);
            }
        };
        callbacks2 = task -> {
            view.hideLoading();
            if (task.isSuccessful() && task.getResult() != null && task.getResult().getUser() != null)
                findUser(task.getResult().getUser());
            else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                view.showErrorMsg(StringUtils.getString(R.string.invalid_verification_code));
        };
    }

    @Override
    protected LiveData<ErrorModel> getErrorLiveData() {
        addErrorObservers(phoneAuthRepo);
        return super.getErrorLiveData();
    }

    private void signInWithCredentials(PhoneAuthCredential credential) {
        phoneAuthRepo.signInWithPhoneAuthCredential(activityInstance, callbacks2, credential);
    }

    private void findUser(FirebaseUser user) {
        onPhoneAuthenticated.addSource(userRepo.getUserById(user.getUid()), userModelOptional -> {
            view.showSuccessMsg(StringUtils.getString(R.string.phone_authenticated_successfully));
            onPhoneAuthenticated.setValue(userModelOptional.orElse(new UserModel(user.getUid(), Objects.requireNonNull(user.getPhoneNumber()))));
        });
    }

    @Override
    protected void onCleared() {
        phoneAuthRepo.dispose();
        super.onCleared();
    }
}
