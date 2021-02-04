package com.msaproject.doctor.ui.appointments.add_appointment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.msaproject.doctor.R;
import com.msaproject.doctor.databinding.DialogAddAppointmentBinding;
import com.msaproject.doctor.utils.StringUtils;

public class AddAppointmentDialog extends Dialog {

    public AddAppointmentDialog(@NonNull Context context, OnAddAppointmentClicked listener) {
        super(context, R.style.Theme_AppCompat_Light_Dialog_Alert);

        DialogAddAppointmentBinding viewBinding = DialogAddAppointmentBinding.inflate(LayoutInflater.from(context), null, false);
        setContentView(viewBinding.getRoot());

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        viewBinding.btnAddAppointment.setOnClickListener(v -> {
            String appointmentTitle = viewBinding.etTitle.getText().toString().trim();
            if (TextUtils.isEmpty(appointmentTitle))
                viewBinding.etTitle.setError(StringUtils.getString(R.string.appointment_title_cannot_be_empty));
            else {
                dismiss();
                listener.onAddAppointment(appointmentTitle);
            }
        });
    }

    public interface OnAddAppointmentClicked {
        void onAddAppointment(String appointmentTitle);
    }
}
