package com.msaproject.doctor.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.msaproject.doctor.ApplicationClass;
import com.msaproject.doctor.R;
import com.msaproject.doctor.di.baseview.BaseViewModule;
import com.msaproject.doctor.di.fragment.DaggerFragmentComponent;
import com.msaproject.doctor.di.fragment.FragmentComponent;
import com.msaproject.doctor.di.viewmodel.DaggerViewModelFactory;
import com.msaproject.doctor.model.ErrorModel;
import com.msaproject.doctor.ui.view.LoadingDialog;
import com.msaproject.doctor.utils.StringUtils;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;

public abstract class BaseFragment<T extends ViewBinding> extends Fragment implements BaseView {

    protected abstract T getViewBinding(LayoutInflater inflater, ViewGroup container);

    protected abstract void inject();

    protected abstract void onViewCreated();

    @Inject
    protected DaggerViewModelFactory viewModelFactory;
    protected T viewBinding;
    protected FragmentComponent daggerComponent;
    protected View view;
    protected LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = getViewBinding(inflater, container);
        view = viewBinding.getRoot();

        initDagger();
        inject();

        initBasicViews();

        onViewCreated();
        return view;
    }

    private void initBasicViews() {
        loadingDialog = new LoadingDialog(getContext());
    }

    private void initDagger() {
        daggerComponent = DaggerFragmentComponent
                .builder()
                .applicationComponent(ApplicationClass.get(requireContext()).getComponent())
                .baseViewModule(new BaseViewModule(this))
                .build();
    }


    protected void onError(ErrorModel errorModel) {
        hideLoading();
        if (errorModel.getCode() == 401 && isAdded()) {
            showErrorMsg("Session Expired");
            /*Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);*/
        } else {
            showErrorMsg(errorModel.getMessage());
        }
    }

    @Override
    public void showErrorMsg(String msg) {
        try {
            Toasty.error(requireContext(), msg, Toasty.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showSuccessMsg(String msg) {
        try {
            Toasty.success(requireContext(), msg, Toasty.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoading(String msg) {
        if (loadingDialog != null && isAdded())
            loadingDialog.show(msg);
    }

    @Override
    public void showLoading() {
        showLoading(StringUtils.getString(R.string.loading_dialog_loading_msg));
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null && isAdded())
            loadingDialog.dismiss();
    }

}
