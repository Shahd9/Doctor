package com.msaproject.doctor.di.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.msaproject.doctor.ui.appointments.AppointmentsViewModel;
import com.msaproject.doctor.ui.complete_register.CompleteRegisterViewModel;
import com.msaproject.doctor.ui.edit_profile.EditProfileViewModel;
import com.msaproject.doctor.ui.host.HostViewModel;
import com.msaproject.doctor.ui.medical_files.MedicalFilesViewModel;
import com.msaproject.doctor.ui.my_patients.MyPatientsViewModel;
import com.msaproject.doctor.ui.notifications.NotificationsViewModel;
import com.msaproject.doctor.ui.phone_auth.PhoneAuthViewModel;
import com.msaproject.doctor.ui.profile.ProfileViewModel;
import com.msaproject.doctor.ui.splash.SplashViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**** IMPORTANT ****
 The module must have at least one @IntoMap ViewModel
 Otherwise the HashMap Won't be created
 And you'll get a dagger compiling error
 */
@Module
abstract public class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindsViewModelFactory(DaggerViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel.class)
    abstract ViewModel provideSplashViewModel(SplashViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PhoneAuthViewModel.class)
    abstract ViewModel providePhoneAuthViewModel(PhoneAuthViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CompleteRegisterViewModel.class)
    abstract ViewModel provideCompleteRegisterViewModel(CompleteRegisterViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HostViewModel.class)
    abstract ViewModel provideHostViewModel(HostViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MyPatientsViewModel.class)
    abstract ViewModel provideMyPatientsViewModel(MyPatientsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NotificationsViewModel.class)
    abstract ViewModel provideNotificationsViewModel(NotificationsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    abstract ViewModel provideProfileViewModel(ProfileViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MedicalFilesViewModel.class)
    abstract ViewModel provideMedicalFilesViewModel(MedicalFilesViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel.class)
    abstract ViewModel provideEditProfileViewModel(EditProfileViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AppointmentsViewModel.class)
    abstract ViewModel provideAppointmentsViewModel(AppointmentsViewModel viewModel);

}
