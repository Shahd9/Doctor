package com.msaproject.doctor.model;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.msaproject.doctor.Constants;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MedicalFileModel implements Serializable {

    public static final String PATIENT_ID = "patientId";

    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName(PATIENT_ID)
    private String patientId;
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

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
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
        modelMap.put(PATIENT_ID, patientId);
        modelMap.put("downloadLink", downloadLink);
        modelMap.put(Constants.MAP_KEY_CREATED_AT, createdAt);
        return modelMap;
    }
}
