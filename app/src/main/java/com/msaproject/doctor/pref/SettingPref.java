package com.msaproject.doctor.pref;


import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SettingPref {

    private final static String KEY_TUTORIAL = "tutorial_show";
    private final static String KEY_LOCALE_SELECT = "languageSelected";
    private final static String KEY_FIRESTORE_DATABASE_VERSION = "languageSelected";

    @Inject
    public SettingPref() {
    }

    public void setShowTutorial(boolean b) {
        SharedPrefManager.getInstance().setBoolean(KEY_TUTORIAL, b);
    }

    public boolean shouldShowTutorial() {
        return SharedPrefManager.getInstance().getBoolean(KEY_TUTORIAL, true);
    }

    public void setIsLangSelected(boolean b) {
        SharedPrefManager.getInstance().setBoolean(KEY_LOCALE_SELECT, b);
    }

    public boolean isLanguageSelected() {
        return SharedPrefManager.getInstance().getBoolean(KEY_LOCALE_SELECT);
    }

    public void setFirestoreDatabaseVersion(int version){
        SharedPrefManager.getInstance().setInteger(KEY_FIRESTORE_DATABASE_VERSION, version);
    }

    public int getFirestoreDatabaseVersion(){
        return SharedPrefManager.getInstance().getInteger(KEY_FIRESTORE_DATABASE_VERSION);
    }
}
