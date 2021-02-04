package com.msaproject.doctor.model.types;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@IntDef({AccountType.DOCTOR, AccountType.PATIENT})
public @interface AccountType {
    int DOCTOR = 1;
    int PATIENT = 2;
}
