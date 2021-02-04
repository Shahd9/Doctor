package com.msaproject.doctor.utils;

import java.util.regex.Pattern;

public class ValidatorUtil {

    public static  boolean isValidPassword(String pass){
        if (pass == null)
            return false;
        return pass.trim().length() >= 6;
    }

    public static  boolean isValidName(String name){
        if (name == null)
            return false;
        return name.length() >= 2;
    }

    public static boolean isValidEmail(String email) {
        if (email == null)
            return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone, int length) {
        if (phone == null)
            return false;
        if (phone.length() != length)
            return false;
        if (Pattern.matches("^[+]?[0-9]{6,13}", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }
}
