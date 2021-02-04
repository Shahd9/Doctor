package com.msaproject.doctor.pref;

import com.google.gson.Gson;
import com.msaproject.doctor.model.UserModel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserPref {

    private static UserModel userModel;

    private final static String KEY_MODEL = "user_model";
    private final static String IS_LOGGED_IN = "is_logged_in";

    @Inject
    public UserPref() {
    }



    public void refreshSavedUser() {
        if (userModel == null)
            return;
        String json = new Gson().toJson(userModel, UserModel.class);
        SharedPrefManager.getInstance().setString(KEY_MODEL, json);
    }

    public void setUserModel(UserModel model) {
        setUserModel(model, true);
    }

    public void setUserModel(UserModel model, boolean isLoggedIn) {
        String json = new Gson().toJson(model, UserModel.class);
        SharedPrefManager.getInstance().setString(KEY_MODEL, json);
        SharedPrefManager.getInstance().setBoolean(IS_LOGGED_IN, isLoggedIn);
        userModel = getUser();
    }

    public UserModel getUser() {
        if (userModel == null) {
            String json = SharedPrefManager.getInstance().getString(KEY_MODEL);
            userModel = new Gson().fromJson(json, UserModel.class);
        }

        return userModel;
    }

    public boolean isLoggedIn() {
        return SharedPrefManager.getInstance().getBoolean(IS_LOGGED_IN);
    }

    public String getId() {
        UserModel model = getUser();
        if (model != null)
            return model.getId();
        return null;
    }

    public void logout() {
        SharedPrefManager.getInstance().clear(KEY_MODEL);
        SharedPrefManager.getInstance().clear(IS_LOGGED_IN);
        userModel = null;
    }

}
