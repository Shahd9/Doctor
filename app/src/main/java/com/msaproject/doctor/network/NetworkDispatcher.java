package com.msaproject.doctor.network;

import com.google.gson.JsonElement;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

class NetworkDispatcher {

    private static Retrofit retrofit;

    private static final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build();

    synchronized static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Endpoints.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return retrofit;
    }


    public interface APIService {

        @Multipart
        @POST
        Observable<JsonElement> postMultiPart(@Url String api, @HeaderMap Map<String, String> headers, @PartMap Map<String, RequestBody> body, @Part MultipartBody.Part file);

        @GET
        Observable<JsonElement> getRequest(@Url String api, @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> param);

        @DELETE
        Observable<JsonElement> deleteRequest(@Url String api, @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> param);

        @POST
        Observable<JsonElement> postRequest(@Url String api, @HeaderMap Map<String, String> headers, @Body Map<String, Object> body);

        @POST
        Observable<JsonElement> postRequest(@Url String api, @HeaderMap Map<String, String> headers, @Body Object body);

        @PUT
        Observable<JsonElement> putRequest(@Url String api, @HeaderMap Map<String, String> headers, @Body Map<String, Object> body);

        @PUT
        Observable<JsonElement> putRequestParam(@Url String api, @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> param);

        @PATCH
        Observable<JsonElement> PatchRequest(@Url String api, @HeaderMap Map<String, String> headers, @Body Map<String, Object> body);

        @PATCH
        Observable<JsonElement> PatchRequestParam(@Url String api, @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> body);

        @PUT
        Completable CompletablePutRequest(@Url String api, @HeaderMap Map<String, String> headers, @Body Map<String, Object> body);

        @POST
        Completable CompletablePostRequest(@Url String api, @HeaderMap Map<String, String> headers, @Body Map<String, Object> body);

        @PATCH
        Completable CompletablePatchRequest(@Url String api, @HeaderMap Map<String, String> headers, @Body Map<String, Object> body);

        @GET
        Observable<ResponseBody> downloadGetRequest(@Url String api);
    }

}
