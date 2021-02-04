package com.msaproject.doctor.di.application;

import android.app.Application;
import android.content.Context;

import com.msaproject.doctor.ApplicationClass;
import com.msaproject.doctor.network.FirebaseManager;
import com.msaproject.doctor.network.NetworkManager;
import com.msaproject.doctor.pref.SettingPref;
import com.msaproject.doctor.pref.UserPref;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component( modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(ApplicationClass app);

    Application getApplication();

    @ApplicationContext
    Context getContext();

    NetworkManager getNetworkManager();

    FirebaseManager getFirebaseManager();

    SettingPref getSettingPref();

    UserPref getUserPref();
}
