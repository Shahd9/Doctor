package com.msaproject.doctor.model.types;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@IntDef({Gender.MALE, Gender.FEMALE})
public @interface Gender {
    int MALE = 1;
    int FEMALE = 2;
}
