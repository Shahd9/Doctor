package com.msaproject.doctor.utils;

import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class PicassoHelper {

    public interface SuccessListener {
        void onSuccess();
    }

    public interface FailureListener {
        void onFailure();
    }

    @Retention(SOURCE)
    @IntDef({MODE.JUST_INTO, MODE.FIT, MODE.FIT_AND_CENTER_CROP, MODE.FIT_AND_CENTER_INSIDE})
    public @interface MODE {
        int JUST_INTO = 0;
        int FIT = 1;
        int FIT_AND_CENTER_CROP = 2;
        int FIT_AND_CENTER_INSIDE = 3;
    }

    public static void loadImageWithCache(
            final String url, ImageView imageView, @MODE int mode, @Nullable Integer errorResID, @Nullable SuccessListener successListener, @Nullable FailureListener failureListener) {

        if (TextUtils.isEmpty(url)) {
            if (failureListener != null)
                failureListener.onFailure();
            return;
        }

        RequestCreator requestCreator = Picasso.get()
                .load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .noFade();

        if (mode >= MODE.FIT)
            requestCreator = requestCreator.fit();
        if (mode == MODE.FIT_AND_CENTER_CROP)
            requestCreator = requestCreator.centerCrop();
        else if (mode == MODE.FIT_AND_CENTER_INSIDE)
            requestCreator = requestCreator.centerInside();

        requestCreator.into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                if (successListener != null)
                    successListener.onSuccess();
            }

            @Override
            public void onError(Exception e) {
                loadImageSkippingCache(url, imageView, mode, errorResID, successListener, failureListener);
            }
        });
    }


    // Access can be public
    private static void loadImageSkippingCache(
            final String url, ImageView imageView, @MODE int mode, @Nullable Integer errorResID, @Nullable SuccessListener successListener, @Nullable FailureListener failureListener) {

        if (TextUtils.isEmpty(url)) {
            if (failureListener != null)
                failureListener.onFailure();
            return;
        }

        RequestCreator requestCreator = Picasso.get()
                .load(url)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .noFade();

        if (mode >= MODE.FIT)
            requestCreator = requestCreator.fit();
        if (mode == MODE.FIT_AND_CENTER_CROP)
            requestCreator = requestCreator.centerCrop();
        else if (mode == MODE.FIT_AND_CENTER_INSIDE)
            requestCreator = requestCreator.centerInside();

        if (errorResID != null)
            requestCreator = requestCreator.error(errorResID);
        else
            requestCreator = requestCreator.error(new ColorDrawable(0xFFFFFF));

        requestCreator.into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                if (successListener != null)
                    successListener.onSuccess();
            }

            @Override
            public void onError(Exception e) {
                if (failureListener != null)
                    failureListener.onFailure();
            }
        });
    }

}
