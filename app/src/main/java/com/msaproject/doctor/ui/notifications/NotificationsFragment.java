package com.msaproject.doctor.ui.notifications;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import com.msaproject.doctor.base.BaseFragment;
import com.msaproject.doctor.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends BaseFragment<FragmentNotificationsBinding> {

    private NotificationsViewModel viewModel;

    @Override
    protected FragmentNotificationsBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentNotificationsBinding.inflate(inflater, container, false);
    }

    @Override
    protected void inject() {
        daggerComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(NotificationsViewModel.class);
    }

    @Override
    protected void onViewCreated() {

    }
}
