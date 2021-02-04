package com.msaproject.doctor.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.StringRes;

import com.msaproject.doctor.ApplicationClass;
import com.msaproject.doctor.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class StringUtils {

    public static String getString(@StringRes int resId) {
        try {
            return ApplicationClass.getContext().getResources().getString(resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getString(@StringRes int resId, Object... formatArgs) {
        try {
            return ApplicationClass.getContext().getResources().getString(resId, formatArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getLanguage() {
        String locale = "en";
        try {
            locale = LocaleHelper.getLanguage(ApplicationClass.getContext());
        } catch (Exception e) {
            locale = Locale.getDefault().getLanguage();
        }

        if (!locale.equals("ar") && !locale.equals("en"))
            locale = "en";
        return locale;
    }

    //remove zero from the beginning of the phone
    public static void setPhoneTextWatcher(EditText etMobile) {
        if (etMobile == null)
            return;

        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phone = etMobile.getText().toString();
                if (phone.startsWith("0") && phone.length() > 1) {
                    etMobile.setText(phone.substring(1));
                    etMobile.setSelection(etMobile.getText().length());
                }
            }
        });
    }

    public static String formatDayToLocale(String day) {
        SimpleDateFormat dateFormatFrom = new SimpleDateFormat("E", Locale.US);
        SimpleDateFormat dateFormat = new SimpleDateFormat("E", new Locale(getLanguage()));
        try {
            Date d = dateFormatFrom.parse(day);
            day = dateFormat.format(Objects.requireNonNull(d));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return day;
    }

    public static String formatDate(String date) {
        String res = date;
        SimpleDateFormat dateFormatFrom = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", new Locale(getLanguage()));
        try {
            Date d = dateFormatFrom.parse(date);
            res = dateFormat.format(Objects.requireNonNull(d));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String formatDate2(String date) {
        String res = date;
        SimpleDateFormat dateFormatFrom = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy", new Locale(getLanguage()));
        try {
            Date d = dateFormatFrom.parse(date);
            res = dateFormat.format(Objects.requireNonNull(d));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String formatDate3(String date) {
        String res = date;
        SimpleDateFormat dateFormatFrom = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy", new Locale(getLanguage()));
        try {
            Date d = dateFormatFrom.parse(date);
            res = dateFormat.format(Objects.requireNonNull(d));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String formatTime2(String date) {
        if (date == null || date.isEmpty())
            return "";

        String res = date;
        SimpleDateFormat dateFormatFrom = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        try {
            Date d = dateFormatFrom.parse(date);
            res = dateFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String formatTime(String time) {
        String hoursText = time.split(":")[0];
        String minutesText = time.split(":")[1];

        int hour = Integer.parseInt(hoursText);
        int min = Integer.parseInt(minutesText);

        String format;
        if (hour == 0) {
            hour += 12;
            format = StringUtils.getString(R.string.am);
        } else if (hour == 12) {
            format = StringUtils.getString(R.string.pm);
        } else if (hour > 12) {
            hour -= 12;
            format = StringUtils.getString(R.string.pm);
        } else {
            format = StringUtils.getString(R.string.am);
        }

        String t = String.format(new Locale(getLanguage()), "%02d:%02d", hour, min);

        return new StringBuilder().append(t)
                .append(" ").append(format).toString();
    }

    public static String formatDateToLocale(String date, String pattern, Locale locale) {
        try {
            Date d = new SimpleDateFormat(pattern, Locale.ENGLISH).parse(date);
            if (d != null)
                date = new SimpleDateFormat(pattern, locale).format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date);
        return date;
    }

    public static String formatDateToLocale(Date date, String pattern, Locale locale) {
        return new SimpleDateFormat(pattern, locale).format(date);
    }

}
