package com.msaproject.doctor.base;

public interface BaseView {
    void showErrorMsg(String msg);
    void showSuccessMsg(String msg);
    void showLoading();
    void showLoading(String msg);
    void hideLoading();
}
