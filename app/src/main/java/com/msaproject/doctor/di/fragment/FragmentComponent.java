package com.msaproject.doctor.di.fragment;

import com.msaproject.doctor.di.application.ApplicationComponent;
import com.msaproject.doctor.di.baseview.BaseViewModule;
import com.msaproject.doctor.di.viewmodel.ViewModelModule;
import com.msaproject.doctor.ui.my_patients.MyPatientsFragment;
import com.msaproject.doctor.ui.notifications.NotificationsFragment;
import com.msaproject.doctor.ui.profile.ProfileFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = ApplicationComponent.class, modules = {ViewModelModule.class, BaseViewModule.class,})
public interface FragmentComponent {
    void inject(MyPatientsFragment fragment);
    void inject(NotificationsFragment fragment);
    void inject(ProfileFragment fragment);
}
