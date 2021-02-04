package com.msaproject.doctor.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.msaproject.doctor.model.ErrorModel;
import com.msaproject.doctor.pref.UserPref;
import com.msaproject.doctor.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import timber.log.Timber;

@Singleton
public class NetworkManager {

    @Inject
    UserPref userPref;

    @Inject
    NetworkManager() {
    }

    private final static Map<String, String> headers = new HashMap<String, String>() {{
        put("Accept", "application/json");
    }};

    public void updateToken() {
        String token = null;//userPref.getAccessToken();
        if (token != null)
            headers.put("Authorization", "Bearer ".concat(token));

        headers.put("Accept-Language", StringUtils.getLanguage());
    }

    public <T> Observable<T> getRequest(String api, Map<String, Object> param, Class<T> parseClass) {
        if (param == null)
            param = new HashMap<>();

        updateToken();

        Observable<JsonElement> call = NetworkDispatcher.getRetrofit().create(NetworkDispatcher.APIService.class)
                .getRequest(api, headers, param);

        return call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(jsonElement -> parseResponse(jsonElement, parseClass));
    }

    public <T> Observable<T> deleteRequest(String api, Map<String, Object> param, Class<T> parseClass) {
        if (param == null)
            param = new HashMap<>();

        updateToken();

        Observable<JsonElement> call = NetworkDispatcher.getRetrofit().create(NetworkDispatcher.APIService.class)
                .deleteRequest(api, headers, param);

        return call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(jsonElement -> parseResponse(jsonElement, parseClass));
    }

    public <T> Observable<T> postRequest(String api, Map<String, Object> body, Class<T> parseClass) {
        if (body == null)
            body = new HashMap<>();

        updateToken();
        Observable<JsonElement> call = NetworkDispatcher.getRetrofit().create(NetworkDispatcher.APIService.class)
                .postRequest(api, headers, body);

        return call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(jsonElement -> parseResponse(jsonElement, parseClass));
    }

    public <T> Observable<T> postMultiPart(String api, Map<String, RequestBody> body, MultipartBody.Part file, Class<T> parseClass) {

        updateToken();
        Observable<JsonElement> call = NetworkDispatcher.getRetrofit().create(NetworkDispatcher.APIService.class)
                .postMultiPart(api, headers, body, file);

        return call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(jsonElement -> parseResponse(jsonElement, parseClass));
    }

    public <T> Observable<T> postRequest(String api, Object body, Class<T> parseClass) {
        if (body == null)
            body = "";


        updateToken();
        Observable<JsonElement> call = NetworkDispatcher.getRetrofit().create(NetworkDispatcher.APIService.class)
                .postRequest(api, headers, body);

        return call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(jsonElement -> parseResponse(jsonElement, parseClass));
    }

    public <T> Observable<T> putRequest(String api, Map<String, Object> body, Class<T> parseClass) {
        if (body == null)
            body = new HashMap<>();

        updateToken();
        Observable<JsonElement> call = NetworkDispatcher.getRetrofit().create(NetworkDispatcher.APIService.class)
                .putRequest(api, headers, body);

        return call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(jsonElement -> parseResponse(jsonElement, parseClass));
    }

    public <T> Observable<T> putRequestParam(String api, Map<String, Object> param, Class<T> parseClass) {
        if (param == null)
            param = new HashMap<>();

        updateToken();
        Observable<JsonElement> call = NetworkDispatcher.getRetrofit().create(NetworkDispatcher.APIService.class)
                .putRequestParam(api, headers, param);

        return call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(jsonElement -> parseResponse(jsonElement, parseClass));
    }

    public <T> Observable<T> patchRequest(String api, Map<String, Object> body, Class<T> parseClass) {
        if (body == null)
            body = new HashMap<>();

        updateToken();
        Observable<JsonElement> call = NetworkDispatcher.getRetrofit().create(NetworkDispatcher.APIService.class)
                .PatchRequest(api, headers, body);
        return call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(jsonElement -> parseResponse(jsonElement, parseClass));
    }

    public <T> Observable<T> patchRequestParam(String api, Map<String, Object> param, Class<T> parseClass) {
        if (param == null)
            param = new HashMap<>();

        updateToken();
        Observable<JsonElement> call = NetworkDispatcher.getRetrofit().create(NetworkDispatcher.APIService.class)
                .PatchRequestParam(api, headers, param);
        return call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(jsonElement -> parseResponse(jsonElement, parseClass));
    }

    public Completable putRequest(String api, Map<String, Object> body) {
        if (body == null)
            body = new HashMap<>();

        updateToken();
        Completable call = NetworkDispatcher.getRetrofit().create(NetworkDispatcher.APIService.class)
                .CompletablePutRequest(api, headers, body);
        return call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable postRequest(String api, Map<String, Object> body) {
        if (body == null)
            body = new HashMap<>();

        updateToken();
        Completable call = NetworkDispatcher.getRetrofit().create(NetworkDispatcher.APIService.class)
                .CompletablePostRequest(api, headers, body);
        return call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable patchRequest(String api, Map<String, Object> body) {
        if (body == null)
            body = new HashMap<>();

        updateToken();
        Completable call = NetworkDispatcher.getRetrofit().create(NetworkDispatcher.APIService.class)
                .CompletablePatchRequest(api, headers, body);
        return call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private <T> T parseResponse(JsonElement jsonElement, Class<T> parseClass) throws ErrorModel {
        try {
            Gson gson = new GsonBuilder().serializeNulls().create();

            if (jsonElement.getAsJsonObject().get("data") == null) {
                JsonArray errors = jsonElement.getAsJsonObject().getAsJsonArray("errors");
                throw gson.fromJson(errors.get(0), ErrorModel.class);
            }

            return gson.fromJson(jsonElement.getAsJsonObject().get("data"), parseClass);
        } catch (Exception e) {
            Timber.e(e);
            throw e;
        }
    }

    public Observable<ResponseBody> getDownloadRequest(String api) {
        Observable<ResponseBody> call = NetworkDispatcher.getRetrofit().create(NetworkDispatcher.APIService.class).downloadGetRequest(api);
        return call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
