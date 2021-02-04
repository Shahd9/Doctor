package com.msaproject.doctor.model.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class UploadStatusResponse {

    @NonNull
    @SerializedName("isDone")
    private Boolean isDone;
    @NonNull
    @SerializedName("progress")
    private Integer progress;
    @Nullable
    @SerializedName("downloadLink")
    private String downloadLink;

    public UploadStatusResponse(@NonNull Boolean isDone, @NonNull Integer progress, @Nullable String downloadLink) {
        this.isDone = isDone;
        this.progress = progress;
        this.downloadLink = downloadLink;
    }

    @NonNull
    public Boolean getDone() {
        return isDone;
    }

    @NonNull
    public Integer getProgress() {
        return progress;
    }

    @Nullable
    public String getDownloadLink() {
        return downloadLink;
    }

}
