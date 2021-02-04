package com.msaproject.doctor.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.msaproject.doctor.ApplicationClass;
import com.msaproject.doctor.R;
import com.msaproject.doctor.di.activity.ActivityComponent;
import com.msaproject.doctor.di.activity.DaggerActivityComponent;
import com.msaproject.doctor.di.baseview.BaseViewModule;
import com.msaproject.doctor.di.viewmodel.DaggerViewModelFactory;
import com.msaproject.doctor.model.ErrorModel;
import com.msaproject.doctor.ui.view.LoadingDialog;
import com.msaproject.doctor.utils.LocaleHelper;
import com.msaproject.doctor.utils.StringUtils;

import java.util.Objects;

import javax.inject.Inject;
import es.dmoral.toasty.Toasty;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity implements BaseView {

    protected abstract T getViewBinding();

    protected abstract void inject();

    protected abstract void onViewCreated();

    @Inject
    protected DaggerViewModelFactory viewModelFactory;
    protected T viewBinding;
    protected ActivityComponent daggerComponent;
    protected LoadingDialog loadingDialog;
    private boolean doubleBackToExitPressedOnce = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (StringUtils.getLanguage().equals("ar"))
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        else
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        viewBinding = getViewBinding();
        setContentView(viewBinding.getRoot());

        initDagger();
        inject();

        initBasicViews();

        onViewCreated();
    }

    protected void onError(ErrorModel errorModel) {
        hideLoading();
        if (errorModel.isAuthError()) {
            showErrorMsg("Session Expired");
            /*Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);*/
        } else {
            showErrorMsg(errorModel.getMessage());
        }
    }

    private void initBasicViews() {
        loadingDialog = new LoadingDialog(this);
    }

    private void initDagger() {
        daggerComponent = DaggerActivityComponent.builder()
                .applicationComponent(ApplicationClass.get(this).getComponent())
                .baseViewModule(new BaseViewModule(this))
                .build();
    }

    protected void enableDoubleBackExit() {
        doubleBackToExitPressedOnce = false;
    }

    @Override
    public void showErrorMsg(String msg) {
        Toasty.error(this, msg, Toasty.LENGTH_LONG).show();
    }

    @Override
    public void showSuccessMsg(String msg) {
        Toasty.success(this, msg, Toasty.LENGTH_LONG).show();
    }

    @Override
    public void showLoading(String msg) {
        hideKeyboard();
        if (loadingDialog != null)
            loadingDialog.show(msg);
    }

    @Override
    public void showLoading() {
        showLoading(StringUtils.getString(R.string.loading_dialog_loading_msg));
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }

    protected void setTitleWithBack(String title) {
        setTitle(title);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    protected void showKeyboard(@NonNull View focusedView) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).showSoftInput(focusedView, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = getCurrentFocus();
            if (view == null) {
                view = new View(this);
            }
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toasty.normal(this, StringUtils.getString(R.string.click_twice)).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}
