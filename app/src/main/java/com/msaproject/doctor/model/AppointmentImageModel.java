package com.msaproject.doctor.model;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.msaproject.doctor.Constants;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AppointmentImageModel {

    public static final String APPOINTMENT_ID = "appointmentId";

    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName(APPOINTMENT_ID)
    private String appointmentId;
    @Expose
    @SerializedName("downloadLink")
    private String downloadLink;
    @Expose
    @SerializedName(Constants.MAP_KEY_CREATED_AT)
    @ServerTimestamp
    private Date createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Map<String, Object> getModelMap() {
        HashMap<String, Object> modelMap = new HashMap<>();
        modelMap.put("id", id);
        modelMap.put(APPOINTMENT_ID, appointmentId);
        modelMap.put("downloadLink", downloadLink);
        modelMap.put(Constants.MAP_KEY_CREATED_AT, createdAt);
        return modelMap;
    }
}
