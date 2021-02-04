package com.msaproject.doctor.model.request;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class MultipleUploadRequestItemModel {

    @NonNull
    @SerializedName("folderId")
    private String folderId;
    @NonNull
    @SerializedName("fileNameOnStorage")
    private String fileNameOnStorage;
    @NonNull
    @SerializedName("extensionOnStorage")
    private String extensionOnStorage;
    @NonNull
    @SerializedName("photoUri")
    private Uri photoUri;

    public MultipleUploadRequestItemModel(@NonNull String folderId, @NonNull String fileNameOnStorage, @NonNull String extensionOnStorage, @NonNull Uri photoUri) {
        this.folderId = folderId;
        this.fileNameOnStorage = fileNameOnStorage;
        this.extensionOnStorage = extensionOnStorage;
        this.photoUri = photoUri;
    }

    @NonNull
    public String getFolderId() {
        return folderId;
    }

    @NonNull
    public String getFileNameOnStorage() {
        return fileNameOnStorage;
    }

    @NonNull
    public String getExtensionOnStorage() {
        return extensionOnStorage;
    }

    @NonNull
    public Uri getPhotoUri() {
        return photoUri;
    }
}
