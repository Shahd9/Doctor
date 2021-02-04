package com.msaproject.doctor.di.activity;

import com.msaproject.doctor.di.application.ApplicationComponent;
import com.msaproject.doctor.di.baseview.BaseViewModule;
import com.msaproject.doctor.di.viewmodel.ViewModelModule;
import com.msaproject.doctor.ui.appointments.appointment_view_edit.AppointmentDetailsActivity;
import com.msaproject.doctor.ui.appointments.appointments_list.AppointmentsListActivity;
import com.msaproject.doctor.ui.appointments.appointment_view_edit.EditAppointmentActivity;
import com.msaproject.doctor.ui.complete_register.CompleteRegisterActivity;
import com.msaproject.doctor.ui.edit_profile.EditProfileActivity;
import com.msaproject.doctor.ui.host.HostActivity;
import com.msaproject.doctor.ui.medical_files.MedicalFilesActivity;
import com.msaproject.doctor.ui.onboarding.OnBoardingActivity;
import com.msaproject.doctor.ui.patient_details.PatientDetailsActivity;
import com.msaproject.doctor.ui.phone_auth.PhoneAuthActivity;
import com.msaproject.doctor.ui.splash.SplashActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = {ViewModelModule.class, BaseViewModule.class,})
public interface ActivityComponent {
    void inject(SplashActivity activity);
    void inject(OnBoardingActivity activity);
    void inject(PhoneAuthActivity activity);
    void inject(CompleteRegisterActivity activity);
    void inject(HostActivity activity);
    void inject(MedicalFilesActivity activity);
    void inject(EditProfileActivity activity);
    void inject(PatientDetailsActivity activity);
    void inject(EditAppointmentActivity activity);
    void inject(AppointmentsListActivity activity);
    void inject(AppointmentDetailsActivity activity);
}
