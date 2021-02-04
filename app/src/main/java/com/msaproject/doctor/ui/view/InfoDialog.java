package com.msaproject.doctor.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.msaproject.doctor.R;
import com.msaproject.doctor.databinding.DialogInfoLayoutBinding;


public class InfoDialog extends Dialog {


    private DialogInfoLayoutBinding viewBinding;
    private OnDialogActionListener listener;

    public InfoDialog(@NonNull Context context, OnDialogActionListener listener) {
        super(context, R.style.Theme_AppCompat_Light_Dialog_Alert);

        viewBinding = DialogInfoLayoutBinding.inflate(LayoutInflater.from(context), null, false);
        setContentView(viewBinding.getRoot());

        this.listener = listener;

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void show(int imageID, String message, String confirmText, String cancelText) {
        if (imageID == -1) {
            viewBinding.ivImage.setVisibility(View.GONE);

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 0);
            viewBinding.tvMessage.setLayoutParams(params);

            viewBinding.tvMessage.setPadding(convertDpToPixel(16), convertDpToPixel(24), convertDpToPixel(16), 0);
        } else viewBinding.ivImage.setImageResource(imageID);

        viewBinding.tvMessage.setText(message);

        if (confirmText == null)
            viewBinding.btnConfirm.setVisibility(View.GONE);
        else
            viewBinding.btnConfirm.setText(confirmText);

        if (cancelText == null)
            viewBinding.btnCancel.setVisibility(View.GONE);
        else
            viewBinding.btnCancel.setText(cancelText);

        viewBinding.btnConfirm.setOnClickListener(v -> listener.onConfirm());
        viewBinding.btnCancel.setOnClickListener(v -> listener.onCancel());

        show();
    }

    private int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public interface OnDialogActionListener {
        void onCancel();

        void onConfirm();
    }
}
