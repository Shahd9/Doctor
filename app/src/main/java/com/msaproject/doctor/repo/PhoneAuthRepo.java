package com.msaproject.doctor.repo;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.msaproject.doctor.base.BaseRepo;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class PhoneAuthRepo extends BaseRepo {

    private final FirebaseAuth firebaseAuth;

    @Inject
    public PhoneAuthRepo() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void verifyPhoneNumber(Activity activity, PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks, String phoneNumber){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(activity)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void signInWithPhoneAuthCredential(Activity activity, OnCompleteListener<AuthResult> callbacks, PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, callbacks);
    }
}
